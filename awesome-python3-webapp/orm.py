#!/usr/bin/env python3
# -*- coding: utf-8 -*-



import asyncio, logging
import aiomysql


def log(sql, args=()):
    logging.info('SQL: %s' % sql)

async def create_pool(loop, **kw):
    logging.info('create database connection pool')
    global __pool
    __pool = await aiomysql.create_pool(
        host=kw.get('host', 'localhost'),
        port=kw.get('port', 3306),
        user=kw.get('user'),
        password=kw.get('password'),
        db=kw.get('db'),
        charset=kw.get('charset', 'utf8'),
        autocommit=kw.get('autocommit', True),
        maxsize=kw.get('maxsize', 10),
        minsize=kw.get('minsize', 1),
        loop=loop
    )

'''
    size表示获取几条信息
'''
async def select(sql, args, size=None):
    log(sql, args)
    global __pool
    async with __pool.get() as connection:
        async with connection.cursor(aiomysql.DictCursor) as cursor:
            await cursor.execute(sql.replace('?', '%s'), args or ())
            if size:
                result = await cursor.fetchmany(size)
            else:
                result = await cursor.fetchall()
    logging.info('rows returned:%s' % len(result))
    return result


'''
    insert,update,delete可定义通用的函数，因为他们的参数是一致的
    autocommit暂时没弄懂啥意思
'''
async def execute(sql, args, autocommit=True):
    log(sql)
    async with __pool.get() as connection:
        if not autocommit:
            await connection.begin()
        try:
            async with connection.cursor(aiomysql.DictCursor) as cursor:
                await cursor.execute(sql.replace('?', "%s"), args)
                affect_db_row = cursor.rowcount
            if not autocommit:
                await connection.commit()
        except BaseException as e:
            if not autocommit:
                await connection.rollback()
            raise
        return affect_db_row


def create_args_string(num):
    L = []
    for n in range(num):
        L.append('?')
    return ', '.join(L)


class Field(object):

    def __init__(self, name, column_type, primary_key, default):
        self.name = name
        self.column_type = column_type
        self.primary_key = primary_key
        self.default = default

    def __str__(self):
        return '<%s, %s:%s>' % (self.__class__.__name__, self.column_type, self.name)


class StringField(Field):

    def __init__(self, name=None, primary_key=False, default=None, ddl='varchar(100)'):
        super().__init__(name, ddl, primary_key, default)


class BooleanField(Field):

    def __init__(self, name=None, default=False):
        super().__init__(name, 'boolean', False, default)


class IntegerField(Field):

    def __init__(self, name=None, primary_key=False, default=0):
        super().__init__(name, 'bigint', primary_key, default)


class FloatField(Field):

    def __init__(self, name=None, primary_key=False, default=0.0):
        super().__init__(name, 'real', primary_key, default)


class TextField(Field):

    def __init__(self, name=None, default=None):
        super().__init__(name, 'text', False, default)


class ModelMetaclass(type):

    def __new__(cls, name, baseclasses, attrs):
        if name == 'Model':
            return type.__new__(cls, name, baseclasses, attrs)

        # 获取表名称，如果User类中没有定义__table__属性，那么类名就作为表名
        tableName = attrs.get('__table__', None) or name

        logging.info('found model: %s (table: %s)' % (name, tableName))

        # 获取所有的Field和主键名
        mappings = dict()
        fields = []
        primaryKey = None
        for key, value in attrs.items():
            if isinstance(value, Field):
                logging.info(' found mapping: %s ==> %s' % (key, value))
                mappings[key] = value
                if value.primary_key:
                    # 找到主键
                    if primaryKey:
                        raise RuntimeError('Duplicate primary key for field: %s' % k)
                    primaryKey = key
                else:
                    fields.append(key)
        if not primaryKey:
            raise RuntimeError('Primary key not found.')

        for key in mappings.keys():
            attrs.pop(key)

        escaped_fields = list(map(lambda f: '`%s`' % f, fields))

        # 这里讲集成Model类的子类中的属性或方法都作为一个dict保存进了attrs中的__mappings__中
        attrs['__mappings__'] = mappings  # 保存属性和列的映射关系
        attrs['__table__'] = tableName
        attrs['__primary_key__'] = primaryKey  # 主键属性
        attrs['__fields__'] = fields  # 除主键外的属性名

        # 构造默认的SELECT, INSERT, UPDATE和DELETE语句:
        attrs['__select__'] = 'select `%s`, %s from `%s`' % (primaryKey, ', '.join(escaped_fields), tableName)
        attrs['__insert__'] = 'insert into `%s` (%s, `%s`) values (%s)' % (tableName, ', '.join(escaped_fields), primaryKey, create_args_string(len(escaped_fields) + 1))
        attrs['__update__'] = 'update `%s` set %s where `%s`=?' % (tableName, ', '.join(map(lambda f: '`%s`=?' % (mappings.get(f).name or f), fields)), primaryKey)
        attrs['__delete__'] = 'delete from `%s` where `%s`=?' % (tableName, primaryKey)

        return type.__new__(cls, name, baseclasses, attrs)


class Model(dict, metaclass=ModelMetaclass):

    def __init__(self, **kw):
        super(Model, self).__init__(**kw)

    def __getattr__(self, key):
        try:
            return self[key]
        except KeyError:
            raise AttributeError(r"'Model'object has no attribute '%s'" % key)

    def __setattr__(self, key, value):
        self[key] = value

    def getValue(self,key):
        return getattr(self, key, None)

    # 没太看明白这个函数
    def getValueOrDefault(self, key):
        value = getattr(self, key, None)
        if value is None:
            field = self.__mappings__[key]
            if field.default is not None:
                value = field.default() if callable(field.default) else field.default
                logging.debug('using default value for %s: %s' % (key, str(value)))
                setattr(self, key, value)
        return value


    @classmethod
    async def findAll(cls, where=None, args=None, **kw):
        'find obejcts by where clause'
        sql = [cls.__select__]
        if where:
            sql.append('where')
            sql.append(where)

        if args is None:
            args = []

        orderBy = kw.get('orderBy', None)
        if orderBy:
            sql.append('order by')
            sql.append(orderBy)

        limit = kw.get('limit', None)
        if limit is not None:
            sql.append('limit')
            if isinstance(limit, int):
                sql.append('?')
                args.extend(limit)
            elif isinstance(limit, tuple) and len(limit)==2:
                sql.append('?,?')
                args.extend(limit)
            else:
                raise ValueError('Invalid limit value: %s' % str(limit))

        result = await select(' '.join(sql), args)
        # 将每一个返回的结果
        return [cls(**r) for r in result]

    @classmethod
    async def findNumber(cls, selectField, where=None, args=None):
        ' find number by select and where. '
        sql = ['select %s _num_ from `%s`' % (selectField, cls.__table__)]
        if where:
            sql.append('where')
            sql.append(where)
        rs = await select(' '.join(sql), args, 1)
        if len(rs) == 0:
            return None
        return rs[0]['_num_']

    @classmethod
    async def find(cls, pk):
        ' find object by primary key. '
        rs = await select('%s where `%s`=?' % (cls.__select__, cls.__primary_key__), [pk], 1)
        if len(rs) == 0:
            return None
        return cls(**rs[0])


async def save(self):
    args = list(map(self.getValueOrDefault, self.__fields__))
    args.append(self.getValueOrDefault(self.__primary_key__))
    rows = await execute(self.__insert__, args)
    if rows != 1:
        logging.warn('failed to insert record: affected rows: %s' % rows)


async def update(self):
    args = list(map(self.getValue, self.__fields__))
    args.append(self.getValue(self.__primary_key__))
    rows = await execute(self.__update__, args)
    if rows != 1:
        logging.warn('failed to update by primary key: affected rows: %s' % rows)


async def remove(self):
    args = [self.getValue(self.__primary_key__)]
    rows = await execute(self.__delete__, args)
    if rows != 1:
        logging.warn('failed to remove by primary key: affected rows: %s' % rows)









