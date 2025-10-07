#include <stdio.h>
#include <stdlib.h>
#include <math.h>

void enrypt(char *word, int shift)
{
    for (int i = 0; word[i] != '\0'; ++i)
    {

        char letter = word[i];

        if (letter >= 'a' && letter <= 'z')
        {
            word[i] = ('a' + (letter - 'a' + shift) % 26);
        }

        if (letter >= 'A' && letter <= 'Z')
        {
            word[i] = ('A' + (letter - 'A' + shift) % 26);
        }
    }
}

void descrypt(char *word, int shift)
{
    for (int i = 0; word[i] != '\0'; ++i)
    {
        char letter = word[i];

        if (letter >= 'a' && letter <= 'z')
        {
            word[i] = abs('a' + (letter - 'a' + shift) % 26);
        }

        if (letter >= 'A' && letter <= 'Z')
        {
            word[i] = abs('A' + (letter - 'A' - shift) % 26);
        }
        printf("encrypt - %s", word);
    }
}

int main(int args, char *argv[])
{

    if (args != 3)
    {
        printf("Недостаочно аргументов");
        return 1;
    }

    char *word = argv[1];
    int shift = atoi(argv[2]);

    enrypt(word, shift);
    descrypt(word, shift);

    
    return 0;
}
