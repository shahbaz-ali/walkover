<img src="walkover.png" width="150" height="150"/>

# WalkOver
> WalkOver is a library to create simple and light weight web services in Java and Android


Lead Maintainer: [Shahbaz Ali](https://github.com/shahbaz-ali)

 > - Contributor: [Nadeem Bhat](https://github.com/NadeemBhat)


## Introduction
>`walkover` is a simple and easy way of building RESTful web services in Java and Android, Using `walkover` you can create API's using REST architecture. `walkover` has been built keeping simplicity in mind so that developers can focus on the API logic rather than spending time on writing network logic for there applications.

## Installing

>Add `walkover` to your project

#### via Gradle
```groovy
implementation 'io.walkover:walkover:1.0.0'
```
#### via Maven
```xml
<dependency>
<groupId>io.walkover</groupId>
<artifactId>walkover</artifactId>
<version>1.0.0</version>
</dependency>
```
> or download the latest JAR from Maven Central.

#### Features

  * Robust routing
  * Focus on high performance
  * HTTP helpers (redirection, caching, etc)
  
## Quick Start
> The quickest way of getting started with `walkover` is to add the jar into your project and then start using the defined intreface to create API's.

> Create a class which will hold the logic of API's
```java
public class App {

    public App(){ /*Keep empty constructor defined*/ }
    
    //Additional fields if needed 
    
}
```

>Prepare API routes by declaring and annotating your route method by `@RegisterRoute`, and passing the HTTP method and route uri as parameters.
```java
@RegisterRoute(method="GET", route="/api/display")  
    public void httpGetDisplay() {
        /* Do something */
    
        //when you are done
        //return the reponse using
        
        WalkOver.getInstance().write("{\"success\":true,\"message\": \"Your First Api\"}");
    };
```

> Before you starting consuming your API's, you need to register your routes `class` with the `walkover`. To register just call the register method as follows.

```java
WalkOver.getInstance().register(/*pass the instance of your class which holds your routes*/);
```
> In above case you have to pass an instance of `App`, If you are defining you routes in `App` class and also starting the server in same `class` then you can pass `this` as parameter.

```java
WalkOver.getInstance().register(this);
```
> Lastly, you need to start the server to listen on specified port, this is done as
```java
WalkOver.getInstance().listen(/*port*/ 8080);
```

#### Android 
> For Android, activities and fragments should usually register according to their life cycle:

```java
@Override
public void onStart() {
    super.onStart();
    WalkOver.getInstance().register(MyRoute.class);
}

```
Read the full getting started guide.


