#include <stdio.h>


void switching(int **a, int **b){
	int *c = *a;
	int *d = *b;
	*a = d;
	// printf("%d\n",**a);
	*b = c;
}

void increment(int *a){
	*a = *a + 1;
}

int main() {
	int one = 1;
	int two = 2;
	int *first = &one;
	int *second = &two;
	int **f = &first;
	int **s = &second;
	printf("%d\n",**f);
	printf("%d\n",**s);
	switching(f, s);
	printf("%d\n",**f);
	printf("%d\n",**s);
	printf("%d\n",*first );
	increment(first);
	printf("%d\n", *first);
	return 0;
}	


