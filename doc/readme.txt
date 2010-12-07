Purposes
------------
JNDIe provides a (really!) lightweight JNDI environment/server to help
software development. It also implements basic EJB services, for session
beans. In the end, it can be used as an EJB container emulator, mostly
for development and testing purposes.

Basic usage
--------------
- Put a mock-jndi.xml configuration file on your classpath.
- Add jndie.jar (and the other dependent libraries, like commons-digester
  and commons-beanutils) to your classpath. Or use maven:
          <dependency>
              <groupId>com.devx</groupId>
              <artifactId>jndie</artifactId>
              <version>1.0</version>
              <scope>compile</scope>
          </dependency>
- Add "-Djava.naming.factory.initial=com.devx.naming.MockContextFactory"
  to JVM.
- Do your lookup and call your session beans.

Known issues
-----------------
- It doesn’t support entity beans.
- Its transaction support is restricted on each data source.
- It doesn’t check if all methods defined on the remote or local interface
  are implemented by the corresponding session bean class.

History
---------
Work with EJB complexity and long development cycle times can be a pain,
sometimes. Particularly if you need to do the code-deploy-try-fix cycle a
lot of times.

That is what happened to me. On a specific task, I was spending more time
on useless EJB aspects rather producing some useful code. On that task,
I just needed to do a JNDI lookup to get a data source. No EJB specific
thing was required. So, I made a context factory that could do lookups
and return a data source. It was just a library, not a program that I
need to run. I didn’t have to deploy anything to do my tweaks and, for
a time, I was happy.

It’s codename was “JBoss Emulator Tabajara”.

After a few days, I had to make that code be invoked by a session bean
that I’d created, and I need to test that session bean. So, I had to return
to the good and old code-deploy-try-fix cycle. So boring, slow and
unproductive! I had to add some support to session beans to my library!
And I did.

Some coworkers have liked, and asked me to add some other features, like:
- Basic transaction support.
- Check serialization on remote interfaces calls.
- Better configuration file.

Now this baby can run most of our systems. For example, on my development
machine, our site (http://www.guiamais.com.br) runs without JBoss: tomcat
with the “Tabajara” in WEB-INF/lib is enough. Of course, staging,
pre-production and production environments run JBoss and tomcat.

After a few months I was invited to give a lecture about it. So I created
jndie (or simple JNDI and EJB container emulator).

I hope you like it.
