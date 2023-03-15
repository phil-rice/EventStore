A very abstract data store

# Id and Value
There is a service that stores an arbitary string. The id is a url that
includes the SHA of the value so this is 'content addressed' ensuring
that we can trust the value that is returned is the one stored (as long as we check the sha)

# Names are namespace+name
As it implies.

# Name and value
We are using the model of time which says a name's value can change across time.
As we have an immutable store 'logically' the name 'points' to the current value. However, as we have an event store, 
the name actually 'points' to the list of events that set or  modify the value

## Events

### SetValueEvent
This ignores all the events that preceeds it and says 'this is the current value of a name

### SetIdEvent
This ignores all the events that preceeds it and says 'this is the current id of a name'. The id can be turned into a value using the 
id and value service

### LensEvent
This defines a lens that can be used to modify the value of a name. The lens is a function that allows us to modify only part of a value
leaving the rest alone. This is great for things like 'add another line to a list of lines' or 'change some single property'. It 
means that we don't need to store the whole new value object.

The lens language allows us to select items like `a.b.c[3]` and also allows us to append items to a list `a.b.c[append]`

### ZeroEvent
Ignore everything that has gone before and have an empty value.

# Data representation
This data is stored as a string. The string could be any reasonable format. Json support is out of the box, but
it is straightforwards to add support for other formats.

