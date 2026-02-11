#include "stdio.h"
#include "math.h"
#include "stdlib.h"

typedef struct Point {
    int x;
    int y;
} Point;

int distant(Point p1, Point p2)
{
    printf("======\n");
    printf("x = %d, y = %d\n", p1.x, p1.y);
    return sqrt((p2.x - p1.x) * (p2.x - p1.x) + (p2.y - p1.y) * (p2.y - p1.y));
}

void process_point(Point* p) {
    printf("x = %d, y = %d\n", p->x, p->y);
    p->x = 6;
    p->y = 7;
}

double *calculate(Point *points, int num_points) {
    if (!points || num_points <= 0)
        return NULL;

    int num_pairs = num_points / 2;
    double *distances = malloc(num_pairs * sizeof(double));
    if (!distances)
        return NULL;

    for (int i = 0; i < num_pairs; i++)
    { 
        distances[i] = distant(points[2 * i], points[2 * i + 1]);
    }
    return distances;
}
typedef Point (*map)(Point);
int filter(Point* points, int count, Point* results, map func) {
   int j = 0;
    for (int i = 0; i < count; i++) {
        results[j++] = func(points[i]);
    }
    return j;
}

void free_memory(double *ptr) {
    free(ptr); 
}


