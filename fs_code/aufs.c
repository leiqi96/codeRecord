#include <linux/module.h>
#include <linux/fs.h>
#include <linux/pagemap.h>
#include <linux/mount.h>
#include <linux/init.h>
#include <linux/namei.h>

#define AUFS_MAGIC 0x64668735

static struct vfsmount *aufs_mount;
static init aufs_mount_count;

/*
	aufs_get_inode创建一个inode结构，同时要把inode结构假如超级块对象的inode链表头
	inode还要假如一个全局链表inode_in_use
*/

static sturct inode *aufs_get_inode(struct super_block *sb,int mode,dev_t dev)
{
	struct inode *inode = new_inode(sb);//申请一个inode结构体

	//根据inode类型，设置不同的操作函数
	if(inode){
		inode->i_mode = mode;
		inode->i_uid = current->fsuid;
		inode->i_gid = current ->fsgid;
		inode->i_blksize = PAGE_CACHE_SIZE;
		inode->i_blocks = 0;
		inode->i_atime = inode->i_mtime = inode->i_ctime = CURRENT_TIME;
		switch(mode & S_IFMT){
		default:
			init_special_inode(inode,mode,dev);
			break;
		case S_IFREG:
			printk("create a file \n");
			break;
		case S_IFDIR:
			inode->i_op = &simple_dir_inode_operations;
			inode->i_fop = &simple_dir_operations;
			printk("create a dir file\n");
			inode->i_nlink++;
			break;
		}
	}
	return inode;
}



/*SMP safe*/
/*
	parm:dir
		是指parent的inode节点

	aufs_mknod()是通过aufs_get_inode来创建文件的inode，
	然后调用dinstantiate函数把dentry加入到inode的dentry链表头
*/	
static int aufs_mknod(struct inode *dir,struct dentry *dentry,
					int mode,dev_t dev)
{
	struct inode *inode;
	int error = -EPERM;

	if(dentry->d_inode)
		return -EEXIST;

	inode = aufs_get_inode(dir->i_sb,mode,dev);
	if(inode){
		d_instantiate(dentry,inode);
		dget(dentry);
		error = 0;
	}

	return error;
}

/*
	parm:dir  是指parent的inode节点
*/
static int aufs_mkdir(struct inode *dir,struct dentry *dentry,int mode){
	int res;
	//S_IFDIR指示是创建一个目录文件的inode
	res = aufs_mknod(dir,dentry,mode|S_IFDIR,0);
	if(!res){
		dir->i_nlink++;
	}
	retur res;
}

static int aufs_create(struct inode *dir,struct dentry *dentry,int mode)
{
	return aufs_mknod(dir,dentry,mode|S_IFREG,0);
}

static int aufs_fill_super(struct super_block *sb, void *data,int silent)
{
	/*
		tree_descr结构用来描述一些文件
		如果不为空，填充超级块的同时，需要再根目录下创建一些文件
	*/

	static struct tree_descr debug_files[] = { {""}};
	return simple_fill_super(sb,AUFS_MAGIC,debug_files);
}

static struct super_block *aufs_get_sb(struct file_system_type *fs_type,
									int flags, const char *dev_name
									void *data)
{
	return get_sb_single(fs_type, flags,data,aufs_fill_super);
}


static struct file_system_type au_fs_type = {
	.ower 	 = THIS_MODULE.
	.name 	 = "aufs",
	.get_sb  = aufs_get_sb,
	.kill_sb = kill_litter_super,
};


static int aufs_create_by_name(const char *name,mode_t mode,
							struct dentry *parent,
							struct dentry **dentry)
{
	int error = 0;
	/*
		如果没有父目录，就赋予一个，赋予的是前面创建的root dentry
	*/

	if(!parent){
		if(aufs_mount && aufs_mount->mnt_sb){
			parent = aufs_mount->mnt_sb->s_root;
		}
	}

	if(!parent){
		printk("Ah! can not find a parent!\n");
		return -EFAULT;
	}

	*dentry = NULL;
	
	mutex_lock(&parent->d_inode->i_mutex);
	/*
		检查要创建的这个目录存不存在
		首先在父目录下根据名字查找dentry结构，如果存在同名的dentry结构就返回指针
		不存在就创建一个dentry
	*/
	*dentry = lookup_one_len(name, parent, strlen(name));
	if(!IS_ERR(dentry)){
		//如果是目录，调用aufs_mkdir，如果是文件，调用aufs_create
		//aufs_mkdir是用来创建inode
		if((mode & S_IFMT) == S_IFDIR)
			error = aufs_mkdir(parent->d_inode, *dentry, mode);
		else
			error = aufs_create(parent->d_inode, *dentry, mode);
	}else{
		error = PTR_ERR(dentry);
	}
	mutex_unlock(&parent->d_inode->i_mutex);

	return error;
}


struct dentry* aufs_create_file(conts char * name,mode_t mode,
						struct dentry *parent, void * data,
						struct file_operations *fops)
{
	struct dentry *dentry = NULL;
	int error;

	printk("aufs:creating file '%s'\n",name);
	error = aufs_create_by_name(name,mode,parent,&dentry);
	if(error){
		dentry = NULL;
		goto exit;
	}
	if(dentry->d_inode){
		if(data)
			dentry->d_inode->u.generic_ip = data;
		if(fops)
			dentry->d_inode->i_fop = fops;
	}

	exit:
		return dentry;
}

static dentry *aufs_create_dir(const char *name,struct dentry *parent)
{
	return aufs_create_file(name,S_IFDIR | S_IRWXU | S_IRUGO | S_IXUGO,
						parent，NULL,NULL);
}


static void __init aufs_init(void)
{
	int retval;
	struct dentry *pslot;

	//注册sufs文件系统
	retval = register_filesystem(&au_fs_type);

	if(!retval){
		//kern_mount为文件系统分配超级块对象和vfsmount对象
		aufs_mount = kern_mount(&au_fs_type);
		if(IS_ERR(aufs_mount)){
			printk(KERN_ERR "aufs:could not mount!\n");
			unregister_filesystem(&au_fs_type);
			return retval;
		}
	}

	pslot = aufs_create_dir("woman star",NULL);
	aufs_create_file("lbb",S_IFREG|S_IRUGO,pslot,NULL,NULL);
	aufs_create_file("fbb",S_IFREG|S_IRUGO,pslot,NULL,NULL);
	aufs_create_file("ljl",S_IFREG|S_IRUGO,pslot,NULL,NULL);

	pslot = aufs_create_dir("man star",NULL);
	aufs_create_file("ldn",S_IFREG|S_IRUGO,pslot,NULL,NULL);
	aufs_create_file("lcw",S_IFREG|S_IRUGO,pslot,NULL,NULL);
	aufs_create_file("jw",S_IFREG|S_IRUGO,pslot,NULL,NULL);

	return retval;
}

static void _exit aufs_exit(void)
{
	simple_release_fs(&aufs_mount,&aufs_mount_count);
	unregister_filesystem(&au_fs_type);
}

module_init(aufs_init);
module_exit(aufs_exit);
MODULE_LICENSE("GPL");
MOUDLE_DESCRIPTION("this is a simple module");
MODULE_VERSION("version 0.1");