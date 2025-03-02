#ifndef __MOTOR__
#define __MOTOR__

class Motor
{
public:
    virtual void fullyOpen();
    virtual void close();
    virtual void openDegree(int degree);
};

#endif