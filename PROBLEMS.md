<p> 1. In the WordGen lab, you implemented map using a Vector<Association<...>>. Why is a Hashtable a superior data structure? Compare the asymptotic costs of running SearchEngine when using either data structure. <br>
The Hashtable is superior when considering asymptotic analysis: while an access method (such as get()) takes a Vector O(n) runtime, a Hashtable is able to do it with constant time O(1). Additionally, our previous data structure was more like a data structure inside a data structure, which is a little harder to implement and keep track of, while the abstractness of hash codes takes care of that for us.
</p>

<p> 2. In this lab, hash table keys were Strings, which are already designed to work as hash table keys. How would you have to modify an arbitrary class if you wanted to use it as a key in a hash table? You may have to do a little research on your own here to determine how Java hashes an arbitrary key.<br>
The key object should be immutable, which allows you to get the same hash code every time. If desired, the hashcode() and equals() method can be overriden in the arbitrary key's class, but it must honor the contract that “Equal objects must produce the same hash code as long as they are equal, however unequal objects need not produce distinct hash codes.”.
</p>
