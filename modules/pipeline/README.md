The pipeline has three main consumers
* The api project that returns the values over the internet
  * Could be any content type...
  * Obviously here we merge everything into one to return the consumer
* Batch jobs that run typically over one or more namespaces
  * The outputs are sideeffects that do stuff
* A simple method call 'get me this for that'
  * Could be in memory and lightweight ... should feel trivial to set up

So sources
Effectively input is
   list of namespaces
   function from namespace to list of names

A Traversal is not quite right

We then run the event processor... which shouldn't need to be an event processor...decouple this...

   From the inputs we run a kleisli which return returns a result

We may or may not want to merge the values within a namespace
We may or may not want to merge the namespace results

We then run the outputs ... which might have different types / content types in different namespaces
through post processing...

Wow... end up with this... which looks like it's a composible pattern with some work...

For each in Namespace
   For each in Name
      SomeKleisli
      FoldK for names
   FoldK for namespaces
FoldK for all

Want folding pipelines and Monoidal pipelines... This allows massive parallelisation later
Note that here we could have a less FP abstraction and use aggregators to avoid totally wasted heap...
Folding into a map/list needs sensible persistance structures for example... 




Pipeline
   For each input
      SomeKleisli
   Consume result

The result has  parts:
   transforming it which might also do a sideeffect
   aggregating the result into a data structure (aka foldfn/aggregator)
   returning the aggregation





--

Insight:
   a namespace should have a type.













---
Why can't I make up a pipeline in a language, then execute it?

For example

inputs:
   just one namespace/name or many, and what does the output of this stage look like?

Event processing
   One/Many/Merging etc

Post processing
   Pipelines

---

OK why?

Answer: 
* it's 'self service'
* Especially with the post processing doing json transformation

