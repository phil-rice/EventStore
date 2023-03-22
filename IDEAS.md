# Namespaces and schemas

So let's do this. Each namespace has a schema. This includes
* Signature of the rest of the schema - signed by public key of the root namespace
* Root namespace
* Content-Type
* Extension
* StringEncoding (optional. Might be N/A if not a string)
* DataEncodingType
* RequiredEvents 
* Supported {version}s
* DataEncodingInfo - depends on the DataEncodingType

The signature prevents hackers mutating stuff. Especially the 'DataEncodingInfo'
The Content-Type allows us to know how to decode and process the data (e.g. JSON, XML, other, etc)
The Extension is so that when we as humans look at the data we know what it is (e.g. .json, .xml, .txt, .html, etc)
The StringEncoding is so that we know how to decode the data (e.g. UTF-8, UTF-16, etc). 
Supported {version}s will allow us to notify the user that they are using an old client... and that they should upgrade.
DataEncodingInfo is the exciting bit. The rest is just housekeeping.
The required events tells us what events you need to be able to handle to decode the data

# DataEncodingTypes

## JSON
Really simple.  The DataEncodingInfo will be the id of the schema for the JSON. Each line is

{version}\tid\twhen\twho\twhat\t<TheOneLineJson>

## TSV
NOTE It is intended that this can be used when the Content-Type is JSON. Remember we have a schema...

Here we split the data across multiple schemas. Assume we have a data structure like this

# Example: Person -> Address

```json
{"name": "phil", "addresses": [{"address": "a1"}, {"address": "a2"}, {"address": "a3"}]}
```
Assume our namespace is ns.

In ns we would have
```
DateEncodingType=TSV DataEncodingInfo = 'object,person:string,addresses:simplearray'.   // Note the first item is object
{version}\tid\t{length}\twhen\twho\twhat\tPhil\t{signatures}
```

In ns.addresses we would have 
```
DataEncodingType=CSV-UNQUOTED,DataEncodingInfo = 'array,address:string'.                         //Note the first item is array
{version}\tid\t{length}\twhen\twho\twhat\tSetV\ta1\t{signatures}
{version}\tid\t{length}\twhen\twho\twhat\tSetV\ta2\t{signatures}
{version}\tid\t{length}\twhen\twho\twhat\tSetV\ta3\t{signatures}
```

The data in ns stored as

## grandparent -> parent -> child

In grandparent we would have 
```
DataEncodingType=TSV,DataEncodingInfo = 'array,grandparentName: string,parent:array'
{version}\tid\t{length}\twhen\twho\twhat\grandparentName\tAppend\t{signatures}         //note that parent is not here in anyway
```
In parent we would have
```
DataEncodingType=TSV ,DataEncodingInfo = 'array:grandparentId,parentName:string,grandparentId:number,child:array'
{version}\tgrandParentId\t{length}\twhen\twho\twhat\tAppend\t\parentName\tgrandparentId\tchildId\t{signatures}
```
In child we would have 
```
DataEncodingType=TSV, DataEncodingInfo='array:parentId,childName:string,parentId:number'
{version}\tgrandparentId\t\when\twho\twhat\tAppend\tchildname\tparentId\t{signatures}
```

## Future updates
We need to be able to delete items in an array (although most use cases don't need these)
I think most use cases that do need a delete would be 'importing a new set of data' in which case we could have a 'zero' event which means
start again.+

## Events1
* SetValue
* Append
* Zero 

Note that there isn't a 'setId' event in this 


## Motivation

### Speed of indexing large CSV files. 
We can take 100MB+ CSV files and index them quickly. Often these are dumps from a DB
and already have primary key links and stuff, and here we can just that

### Speed of loading 
The rootid determines the filename. We can take a list of namespace/names and work out up front all the filenames for all the names (which might overlap).
Then go get the data, and turn it into JSON objects (one per name - merging the namesspaces)

### Tracability
Every single line has 'who/what/when'

### Security
Every line has one or more signatures (and the data includes the length of the data, which makes the hash dramatically harder to forge)

### Versioning
Every line has a version. This allows us to change the schema and still be able to read old data
