
#include <stdint.h>
#include <stdio.h>
#include <stdlib.h>
#include <time.h>

// include SSE intrinsics
#if defined(_MSC_VER)
#include <intrin.h>
#include <emmintrin.h>
#elif defined(__GNUC__) && (defined(__x86_64__) || defined(__i386__))
#include <x86intrin.h>
#endif


#define CLOCK_RATE_GHZ 2.26e9
static int sum_naive(int n, int *a)
{
    int sum = 0;
        for (int i = 0; i < n; i++)
        {
                sum += a[i];
        printf("number %i\n", a[i]);
        }
        printf("supposeded sum %i\n", sum);
        return sum;
}
static int sum_vectorized(int n, int *a)
{
    // WRITE YOUR VECTORIZED CODE HERE
        __m128i thesum = _mm_setzero_si128();
        int i = 0;
        __m128i toAdd;
        while( i < n - n%4)
        {
                toAdd = _mm_loadu_si128((__m128i*)&a[i]);
                thesum = _mm_add_epi32(thesum, toAdd);
                i += 4;

        }
        int j = 0;
        __m128i toReturn[1];
        _mm_storeu_si128(toReturn, thesum);
        int *toRet = (int*) toReturn;
        int *adding = (int*) &toAdd;

        int theFinalSum = toRet[0] + toRet[1] + toRet[2] + toRet[3];

        printf("first: %i\n",adding[0]);
        printf("second: %i\n",adding[1]);
        printf("third: %i\n",adding[2]);

        printf("fourth: %i\n",adding[3]);


        printf("first degree: %i\n",toRet[0]);
        printf("second degree: %i\n",toRet[1]);
        printf("third degree: %i\n",toRet[2]);

        printf("fourth degree: %i\n",toRet[3]);
        printf("all but the last: %i\n",theFinalSum);

        printf(" current counter %i\n", i);
        while (j < n%4){
                theFinalSum += a[i];
                j += 1;
                printf("current number %i\n", a[i]);
                i += 1;
        }
        printf("the final sum %i\n", theFinalSum);
        return theFinalSum;
}

static int sum_unrolled(int n, int *a)
{
    int sum = 0;

    // unrolled loop
        for (int i = 0; i < n / 4 * 4; i += 4)
    {
        sum += a[i+0];
        sum += a[i+1];
        sum += a[i+2];
        sum += a[i+3];
    }

    // tail case
        for (int i = n / 4 * 4; i < n; i++)
        {
                sum += a[i];
        }

    return sum;
}

static int sum_vectorized_unrolled(int n, int *a)
{
    // UNROLL YOUR VECTORIZED CODE HERE
    __m128i thesum = _mm_setzero_si128();
    int i = 0;
    __m128i toAdd;
    while( i < n - n%16)
    {
            //first add
            toAdd = _mm_loadu_si128((__m128i*)&a[i]);
            thesum = _mm_add_epi32(thesum, toAdd);

            //second add
            toAdd = _mm_loadu_si128((__m128i*)&a[i + 4]);
            thesum = _mm_add_epi32(thesum, toAdd);

            //third add
            toAdd = _mm_loadu_si128((__m128i*)&a[i + 8]);
            thesum = _mm_add_epi32(thesum, toAdd);

            //fourth add
            toAdd = _mm_loadu_si128((__m128i*)&a[i + 12]);
            thesum = _mm_add_epi32(thesum, toAdd);
            i += 16;

    }

    __m128i toReturn[1];
    _mm_storeu_si128(toReturn, thesum);
    int *toRet = (int*) toReturn;
    int *adding = (int*) &toAdd;
    int j = 0;
    int theFinalSum = toRet[0] + toRet[1] + toRet[2] + toRet[3];
    while (j < n%16){
            theFinalSum += a[i];
            j += 1;
            i += 1;
    }
    return theFinalSum;
}

int main(int argc, char **argv)
{
    int array[36];
    int k = 0;
    while (k < 36){
      array[k] = k;
      k += 1;
    }
    printf("%i\n", sum_naive(36, array) );
    printf("%i\n", sum_vectorized(36,array) );
    printf("%i\n", sum_unrolled(36,array) );
    printf("%i\n", sum_vectorized_unrolled(36, array) );
    // const int n = 7;
    //
    // // initialize the array with random values
    //     srand48(time(NULL));
    //     int a[n] __attribute__((aligned(16)));
    //     for (int i = 0; i < n; i++)
    //     {
    //             //making changes to random
    //             a[i] = rand();
    //     }
    //
    // // benchmark series of codes
    //     benchmark(n, a, sum_naive, "naive");
    //     benchmark(n, a, sum_unrolled, "unrolled");
    //     benchmark(n, a, sum_vectorized, "vectorized");
    //     benchmark(n, a, sum_vectorized_unrolled, "vectorized unrolled");
    //
    // return 0;
}
