A very abstract data store that can be accessed through an api or directly from a java client without 
needing a 'api' or anything other than access to the file system for reads. 

# Services

* IdAndValue: Given an id give me the value. This is immutable and the id is the hash of the contents
* NameAndEvents: The value for any given name mutates with time. These mutations are events. 

# Concepts

## Identity
* Name: We have the idea of a 'name' which is the unique key for the thing we are talking about. 
* Namespace: Often the data about a name is smeared across multiple name spaces. A namespace might be 'my banking data', 'data about my hobby', 'personal data' and so on

## Values
The value of an indentity changes across time. The value is a calculated property from events. To get the value
we load the events and evaluate them

## Events
Events mutate the previous value of that identity. 
* SetValueEvent this ignores the previous value and replaces it with a fixed value (stored with the event)
* SetIdEvent this ignores the previous value and replaces it with the value found from the given id in the id2Value store
* ZeroEvent resets the value back to the 'empty' (often 'null')
* LensEvent sets a part of the previous value to a new line.

Typically SetIdEvent or SetValueEvent will be used to set the 'whole value'. This could be used when the next value has little to do with the previous
LensEvents are used to do thinks like 'add a value to a line', 'set the status to X' etc

## Audit
Every event comes with an audit. Who, What and When. Then What is provided by the person creating the event

# Id and Value
This is a low level blob storage tool. The id is the hash of the content. The result is a byte[]

# Usage patterns

## Scripts / transformation
A script or transformation can be set. It would normally use SetIdEvents as the values are 'more than one line long'. The
actual value would be in the id2value store. As the script is mutated its new value is recorded

## Double entry bookkeeping
Each line can be added as LensEvent which would insert a new row into the data.

## Edits of reference data
The reference data (json) can be in the id2value store. The lensevent holds the small amount of data that has been changed
While the original is not actually edited (it is immutable) the accessors will see the changed data


Notes:
Storing scripts/transforms requires the whole script to be stored and perhaps the change is simple... perhaps there is scope for a patch event?