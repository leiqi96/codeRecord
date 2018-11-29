#include <stdio.h>
#include <unistd.h>
#include <stdlib.h>
#include <sys/time.h>
#include <stdint.h>
#include <pthread.h>
#include <sys/eventfd.h>
#include <sys/epoll.h>
 
#define EPOLL_MAX_NUM 10
 
#define handle_error(msg) \
   do { perror(msg); exit(EXIT_FAILURE); } while (0)
 
int efd = -1;
 
void *read_thread(void *arg)
{
	int ret = 0;
	uint64_t count = 0;
	int ep_fd = -1;
	struct epoll_event events[EPOLL_MAX_NUM];
 
	if (efd < 0)
	{
		printf("efd not inited.\n");
		return;
	}
 
	ep_fd = epoll_create(1024);
	if (ep_fd < 0)
	{
		handle_error("epoll_create fail: ");
	}
 
	{
		struct epoll_event read_event;
 
		read_event.events = EPOLLIN;
		read_event.data.fd = efd;  //add eventfd to epoll
 
		ret = epoll_ctl(ep_fd, EPOLL_CTL_ADD, efd, &read_event);
		if (ret < 0)
		{
			handle_error("epoll ctl failed:");
		}
	}
 
	while (1)
	{
		ret = epoll_wait(ep_fd, &events[0], EPOLL_MAX_NUM, -1);
		if (ret > 0)
		{
			int i = 0;
			for (; i < ret; i++)
			{	/*
				if (events[i].events & EPOLLHUP)
				{
					printf("epoll eventfd has epoll hup.\n");
				}
				else if (events[i].events & EPOLLERR)
				{
					printf("epoll eventfd has epoll error.\n");
				}
				else */
				 if (events[i].events & EPOLLIN)
				{
					int event_fd = events[i].data.fd;
					ret = read(event_fd, &count, sizeof(count));
					if (ret < 0)
					{
						handle_error("read fail:");
					}
					else
					{
						struct timeval tv;
 
						gettimeofday(&tv, NULL);
						printf("success read from efd, read %d bytes(%llu) at %lds %ldus\n",
							   ret, count, tv.tv_sec, tv.tv_usec);
					}
				}
			}
		}
		else if (ret == 0)
		{
			/* time out */
			printf("epoll wait timed out.\n");
			break;
		}
		else
		{
			handle_error("epoll wait error:");
		}
	}
 
}
 
int main(int argc, char *argv[])
{
	pthread_t pid = 0;
	uint64_t count = 0;
	int ret,i;
 
	efd = eventfd(0, EFD_NONBLOCK | EFD_CLOEXEC);
	if (efd < 0)
	{
		handle_error("eventfd failed.");
	}
 
	ret = pthread_create(&pid, NULL, read_thread, NULL);
	if (ret < 0)
	{
		handle_error("pthread create:");
	}
 
	for (i = 0; i < 5; i++)
	{
		count = 4;
		ret = write(efd, &count, sizeof(count));
		if (ret < 0)
		{
			handle_error("write event fd fail:");
		}
 
		sleep(1);
	}
	
	printf("write_end\n");	
	
	pthread_join(pid, NULL);
 
	close(efd);
	return 0;
}
