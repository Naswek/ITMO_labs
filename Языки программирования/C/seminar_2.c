#include <stdio.h>
#include <stdlib.h>
#include <string.h>

typedef struct
{
    int x;
    int y;
} Point;

typedef enum
{
    Triangle,
    Square,
    Circle
} ShapeType;

typedef struct
{
    Point p;
    ShapeType type;
    char *name;
} Shape;

typedef struct
{
    Shape *shapes;
    int size;
} Container;

Container *init_container()
{
    Container *ct = malloc(sizeof(Container));
    ct->shapes = NULL;
    ct->size = 0;
    return ct;
}



void add_new_shape(Container * container, Point point, char * name, ShapeType type)
{
    int new_size = container->size + 1;
    Shape *new_shapes = realloc(container->shapes, new_size * sizeof(Shape));

    if (new_shapes == NULL)
    {
        return;
    }

    container->shapes = new_shapes;
    Shape *shape = &container->shapes[container->size];

    shape->p = point;
    shape->type = type;
    shape->name = strdup(name); 

    container->size = new_size;
}

void print(Container * Container)
{
    int size = Container->size;
    printf("size = %d\n", size);
    Shape *shape = Container->shapes;

    for (int i = 0; i < size; i++)
    {
        Shape *shape = &shape[i]; 

        char *name = shape->name; 
        ShapeType shapeType = shape->type;
        Point shapePoints = shape->p;
        int x = shapePoints.x;
        int y = shapePoints.y;

        printf("figure = %d, name = %s, x = %d, y = %d\n", shapeType, name, x, y);
    }
}

void remove_shape_by_index(Container * container, int index)
{
    if (index < 0 || index > container->size) {
        return;
    }

    for (int i = index; i < container->size - 1; i++) {
        container->shapes[i] = container->shapes[i + 1];
    }

   
    Shape *new_shapes = realloc(container->shapes, (container->size - 1) * sizeof(Shape));
    if (new_shapes == NULL) {
        return;
    }
    container->size--;

    if (container->size > 0) {
        container->shapes = new_shapes;
    }
}

int main()
{
    Container *ct = init_container();
    Point *point = malloc(sizeof(Point));
    point->x = 10;
    point->y = 10;
    point = {10, 10}

    add_new_shape(ct, *point, "triangle", Triangle);
    add_new_shape(ct, *point, "circle", Circle);
    print(ct);
    remove_shape_by_index(ct, 0);
    remove_shape_by_index(ct, 1);
    print(ct);

    // TODO:
    // add_new_shape
    // print
    // remove_shape_by_index

    free(ct->shapes);
    free(ct);
    free(point);
    
}