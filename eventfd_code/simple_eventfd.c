#include <sys/eventfd.h>
#include <unistd.h>
#include <stdlib.h>
#include <stdio.h>
#include <stdint.h>             /* Definition of uint64_t */
 
#define handle_error(msg) \
   do { perror(msg); exit(EXIT_FAILURE); } while (0)
 
int
main(int argc, char *argv[])
{
   int efd = eventfd(0, 0);
   int j;
   if (efd == -1)
       handle_error("eventfd");
 
   int ret = fork();
   if(ret == 0) //child
   {
       for (j = 1; j < 10 ; j++) {
           printf("Child writing %d to efd\n", j);
           uint64_t one = j;
           ssize_t s = write(efd, &one, sizeof one);
           if (s != sizeof one)
               handle_error("write");
       }
       printf("Child completed write loop\n");
 
       exit(EXIT_SUCCESS);
   }
   else  //parent
   {
       sleep(2);
       uint64_t one;
       ssize_t s = read(efd, &one, sizeof one);
       if (s != sizeof one)
           handle_error("read");
       printf("Parent read %llu from efd\n",(unsigned long long)one);
       exit(EXIT_SUCCESS);
   }
}
