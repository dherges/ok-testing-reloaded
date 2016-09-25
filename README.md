# ok-testing-reloaded

> Ok, let's go testing HTTP applications with all open-source software.


## Abstract

Topics covered here (five Ts):

* Testing HTTP applications with Square's _"a few ok libraries"_ (Retrofit, OkHttp, Moshi)
* Testing client implementations with a mock web server
* Testing server-side applications with an embedded web server
* Thoughts and ideas on end-to-end/integration testing and dependency injection
* Twitter APIs are utilized for demonstration purposes

---


## Mocking Twitter REST APIs

Let's pretend we're building yet another Twitter client.
We need to communicate with the Twitter REST APIs.
For our example, let's take the following three methods:


[GET statuses/home_timeline](https://dev.twitter.com/rest/reference/get/statuses/home_timeline)

[GET statuses/show/:id](https://dev.twitter.com/rest/reference/get/statuses/show/%3Aid)

[POST statuses/update](https://dev.twitter.com/rest/reference/post/statuses/update)

We copy the example JSON results to the ``src/test/fixtures`` folder, so that we can use them later in automated
testing.


## Writing a first automated test for a Twitter client

Our foolish idea is that we're testing at the HTTP layer.
That means that we set up a [mock web server that replays scripted HTTP responses][mockwebserver].

Then, our client talks to the mock web server so that we know the response in advance, allowing us to make assertions
for our client's behaviour.
When should our client return an error code?
How should the response data be represented in Java classes?

Also, we can inspect the recorded request that the mock web server received and we can answer more questions like:
Did our client include the expected request parameters?

Code can be found in
[``TwitterApiTest``](https://github.com/dherges/ok-testing-reloaded/commit/24b145eaa1ccb7dc11712a0c6d3f8d0e9b798abe).
By looking at it, almost anybody can tell what is going on here: the mock web server is instructed to replay a HTTP
response, the client connects to that server and fetches the response, the test verifies both request and response.

Notice that we reference a ``yaml`` file.
This is a feature of mockwebserver+ that allows to declare a HTTP response in a more readable way.
In ``src/test/fixtures/home_timeline.yaml``, we set the status code and reference a JSON file that is used as the
response body.

Hit the button now and lights will turn green.
Things are working after all.


### Implementing an (almost-)production-ready Twitter client

With our test in place, we can now focus on writing our client.
Since we love Squares's _"ok library stack"_, we chose [Retrofit][retrofit] for our REST/JSON client.

Implementation code can be seen at
[``TwitterApi``](https://github.com/dherges/ok-testing-reloaded/commit/ecf7a5528df917a403125d0bd24cca9773708924).
After changing our test code to use the new ``TwitterApi``, we can re-run our tests against the mocked HTTP web server
and will see that our client still works.
That's how it's supposed to be.

We can now start to add missing features like
[Object to JSON conversion](https://github.com/dherges/ok-testing-reloaded/commit/4d5586fc214c26c6cf21c4b143f2bd9bf7138ec4).
When adding such a feature, we enhance our test case to check that the Java objects are returned from a response.

Since we're a little bit tired of repeating the same assertions over and over again, we'd like to save us a few us a
few keystrokes and introduce some
[custom assertions](http://joel-costigliola.github.io/assertj/assertj-core-custom-assertions.html).
We add a ``RecordedRequestAssert`` and a ``ResponseAssert`` and change our test case to use these assertions.
Then, we realize that we've
[exchanged some 5 lines of code in ``TwitterApiTest`` for some 50 lines of code](https://github.com/dherges/ok-testing-reloaded/commit/2b131d22149f8297fedf3d63e252a4b75abe8850) in our
_newly, fancy, shiny_ assertion classes and wait for that investment to pay off in the future.


### Testing for behaviour on error codes, network delays, and these kind of things

Let's tweet and
[tell the world what music we're listening to](https://github.com/dherges/ok-testing-reloaded/commit/8fa84bd7115c9f5459e2fdeb145cbf536c88f3b4) – right now at that very moment in time.

In ``TwitterApiTest``, we add two more test methods: ``tweet_success()`` and ``tweet_duplicate()``.
In the success case, we let mock web server replay a ``200 Ok`` response with a JSON object of a tweet.

Let's go another round of tweeting.
Twitter says that
["any attempt that would result in duplication will be blocked, resulting in a 403 error"](https://dev.twitter.com/rest/reference/post/statuses/update).
We mimic that behaviour in ``tweet_duplicate()`` by replaying a ``403 Forbidden`` and additionally delaying the
response for some time.
Surely, Twitter wants us to not duplicate duplicate duplicate duplicate duplicate like shit chat but rather calm down.

Where can these feature be really beneficial?
Imagine what can be done with this approach in end-to-end-testing.

From a user's perspective a workflow will look something like:
enter a text, hit a button, _twout_ (= _shout_ + _tweet_) out your words, see a success message and get scrolled up
my timeline seeing my tweet at the very top.
But what if something goes wrong?
There will be an error message, telling me something that I don't understand and eventually inviting me to tweet and
tweet again until it works.

With the ability to replay scripted response we can run through both cases.
We can then write tests that verify application behaviour on success, on a specific error, on a delayed response, and
many more.
End-to-end-testing is a topic of its own that needs an extra article to talk about in depth.


## Summary of client-side testing: skin and bones

With the code that we've written so far we've got a working set up to test a Twitter client.
We feed HTTP responses and JSON documents to the client, putting us in a position where we can dive into the sparkling
times ahead.
It's times like these, you see some benefits again.

Speaking in terms of developer experience, it makes it easy to add more features, talk to new API endpoints,
or check if methods behave in the way that they're expected to be.

There's a little bit of extra confidence that networking, speaking the HTTP protocol, and JSON serialization are
working and they are playing well together.
These are usually the little things that need to fit together when sound turns into music.

For zealous or academic reasons or even both of them, we need to include one more argument:
one could decide to exchange the implementation.
When doing so, remove all the application code, throw away ``TwitterApi``, no longer use Retrofit framework, and just
let the tests survive.
They are still there.
They still serve HTTP responses and JSON documents.
Use the tests to write yet another other Twitter client.

On the back side of the moon, one may grumble that these tests are doing _too much_.
Yes, they are testing library code.
Yes, they are testing integrations of multiple classes.
Is it still what people like to call a _unit_ test?
Has it turned out to be a _functional_ test or something else?

Going on from here, take the code as a skeleton for scaling up.
See it as a source of inspiration or just a bunch of fluky ideas – up to how you perceive it.


---


## Implementing Twitter REST APIs

So far, we've pretended that we're writing a Twitter client.
Now, let's pretend that we've got a job offer from San Francisco and accepted to get paid in
[$TWTR](https://investor.twitterinc.com/stockquote.cfm) common stock.
Now we're hoping for Twitter to turn into the green,
or [Google to pay us out](http://money.cnn.com/2016/09/23/investing/twitter-takeover-rumors-google-salesforce/),
or both of it to happen.
Also, we're now focused on implementing API endpoints on the server side.

For demonstration purposes we will write a server-side web application in [Spark framework][spark].
Spark ships with an embedded Jetty web server and comes with a super simple, expressive programming model.
["Hello, world" says it all](https://github.com/dherges/ok-testing-reloaded/commit/defa715ae7bbf3df20e548f347a04f8967b427c3).


### A set up for integration testing

Similar to our approach for client-side testing, we will be running an HTTP server during the tests and make calls
to the APIs of our application.
The tests then verify that status codes, body contents and so on produce the expected output.

We can use JUnit's ``@Before`` and ``@After`` hooks to bootstrap the application on a web server; or even
``@BeforeClass`` and ``@AfterClass``.
For the purpose of demonstration and learning, let's look at an alternative using a custom Runner.

We remove the hooks from ``TwitterAppTest`` and annotate the class with ``@RunWith(SparkRunner.class)``.
``@RunWith`` tells JUnit to execute the test by creating an instance of ``SparkRunner`` and delegating test logic to
that runner class.
A runner has to find out what test methods should be executed, determine an order of execution, and eventually running
the tests.
For simplicity, we extend the default ``BlockJUnit4ClassRunner`` and add custom logic to bootstrap our web application.

Notice that the bootstrap and shutdown behaviour has moved to ``SparkRunner``.
We've also introduced a custom annotation ``SparkApplicationTest``, telling the runner on what port to start the
embedded web server and which application class to instantiate.
When we're writing more and more tests – maybe for different projects –, we can re-use that and turn a test into an
integration test by simply adding ``@RunWith(SparkRunner.class) @SparkApplicationTest(MyApp.class)``.


### Getting retweets

Before we're getting paid in $TWTR shares, we still need to add some features.
Let's take a go at the retweets API:

[GET statuses/retweets/:id](https://dev.twitter.com/rest/reference/get/statuses/retweets/%3Aid)

A [first implementation](https://github.com/dherges/ok-testing-reloaded/commit/504d9759917ca97fe7b7490d8aacb1a5dc9b6a1d)
of that features comes relatively easy:
register a route with parameters and write a class implementing the application behaviour.
The corresponding test case is also super easy:
make an HTTP request to the embedded web server, look at the response body and make some assertion on it.

However, we can do a little bit better.
Did you notice that we returned a ``String`` from ``RetweetsIdRoute#handle()``?
If we want to return _real enterprisy Java objects_, we need a so-called ``ResponseTranformer`` that converts an
arbitrary object to its string representation.
Since we're suing [Moshi][moshi], let's
[add a ``MoshiResponseTransformer``](https://github.com/dherges/ok-testing-reloaded/commit/a2b3cffa1a3886c1bb345daa7d7c1ab4e35dac64)
that does that job.

Still, in ``RetweetsIdRoute`` we're explicitly setting the content type header.
If we're adding more and more features and writing more routes, we'd had to repeat that line over and over again.
Let's improve that a little bit by
[adding a ``ContentTypeRoute``](https://github.com/dherges/ok-testing-reloaded/commit/cd360819dd62a32b462bc8f38f1b5ec471274d18).
It acts as some kind of intermediary:
when creating a new ``ContentTypeRoute``, we give it a route to delegate application behaviour to, the content type
that should be produced and a ``ResponseTransformer`` that keeps the two of them together.

Then we're super ambitious and move away from an old-school ``for`` loop and do the exact same thing with
[streams and lambda expressions](https://github.com/dherges/ok-testing-reloaded/commit/e5b771d794eed3348167b433816236a18047ee4b).

After changing so much of our application code, does it still do what it's supposed to do?
Let's re-run our test case to find out: yes, it works!


## A consumer's point of view

At this point, our test simply makes a string equality check on the response body.
Whenever the test run gives us a green light we do know that we succeeded in refactoring our implementation with
non-breaking changes.
Now let's change perspectives and try to improve our test case.

When we're looking at the API from a consumer's point of view, what are the things we'd like to know?
A client should know how it can talk to that API and what it should expect in response.
Here, that's the request url and request parameters, status codes and content types of responses, as well as
the structure of response documents or error messages.

Can we write a test case that describes the API in a such way?
Let's try to do so by
[writing the tests in a "prepare request, make a call, assert response"-fashion](https://github.com/dherges/ok-testing-reloaded/commit/0bac45f058b864c031152fea77ab02b66ad95188).
From looking at the test we can tell the request URL and we can tell that we get a JSON document when it's a 200 Ok.

Let me notice that the test example is not complete.
We should find a way to describe that ``200`` is the ``:id`` parameter in ``statuses/retweeets/:id`` and that we can
optionally pass a ``?count=<n>`` query parameter that limits the result list to that number of items.
That's the place where you can jump in.

The official Twitter API documentation always gives an example of a response document.
That example is a full JSON document with all its properties and objects and arrays.
What if we want to point out specific parts of that JSON document that are really really interesting in the current use
case?
Well, an idea is to parse JSON into some objects and then make test assertions on the resulting data.
Here, we use [JSONPath][jsonpath] to crawl through the JSON structure and to point to specific properties or objects.
A custom ``OkHttpResponseAssert#jsonPath()`` extracts data from the given expression and makes an assertion for
equality of the expected value.

A benefit is that JsonPath expressions are not tightened to [one library][jsonpathjayway] or another.
We can give the expressions to any person on that planet and that person will understand that expression.
In theory and practive, that person needs to know how to _'speak'_  or _'understand'_ JSONPath, of course.

If we're looking a little bit further ahead, we can build upon that idea and approach and try to write the tests with
such a verbosity that a test report becomes an API documentation.
Something that I'd like to write more about in the future.


## Dependency Injection and Testing

When people talk about testing it usually doesn't take too much time before the word _dependency injection_ enters
the discussion.
Most of the time, you'll hear someone saying something like
["benefits of using dependency injection (..) is that it makes testing your code easier"](http://google.github.io/dagger/testing.html).

Regarding testing there is one and exactly one motivation for dependency injection:
in production, dependencies are wired up so that all parts fit together to form a rather complex application.
In testing however, some of the dependencies are replaced by
[a stub or a fake or a mock](https://testing.googleblog.com/2013/07/testing-on-toilet-know-your-test-doubles.html).

We use dependency injection because we want to replace a _productive implementation_ by some other implementation.
Whether it is unit or functional or integration or end-to-end testing, motivation and goals remain the same.
We want that replacement to produce well-known input for our application so that we can verify well-known output
produced by our application.


### That giant (s)word

On a side note, I'd like to write a few extra sentences on the topic of dependency injection.
Well, dependency injection sounds like a giant word and there are
[hundreds of thousands of millions of frameworks](https://youtu.be/oK_XtfXPkqw)
out there that cut like a sword in one way or another.

To most people, dependency injection is associated with some kind of 'magic framework' and somehow we need some sort
of 'annotation'.
Personally, I worked in a large project team that [started bleeding thanks to Dagger from Square][dagger] and a few
months later wrote a small project for myself and that got [cut into pieces by Dagger2 from Google][dagger2] eventually.
All the blood came rushing out – yet again – with [Pimple in PHP](http://pimple.sensiolabs.org/),
[BottleJs](https://github.com/young-steveo/bottlejs), or
[Angular2's DI in TypeScript](https://angular.io/docs/ts/latest/guide/dependency-injection.html).
Today is gonna be the day, that we gonna throw it back at'em.

But what is dependency injection?
After all, dependency injection is _a pattern of thinking and organizing_.
When you call your favourite italian restaurant and demand a reservation for one specific table with a view at the sea,
that is **NOT** dependency injection.
Dependency injection is when you go to that restaurant, ask for a table, and the waiter assigns a table to you.
That is just _a pattern how people communicate and interact with each other_.

How does it look like in programming?
Without dependency injection, a class is pro-active and explicitly creates object instances with ``new Something()``.
With dependency injection, that class becomes passive and there is some way of passing an instance of ``Something``
to that class.


### Magic and no magic at all

In order to make our application ready for dependency injection, we introduce something that we call an
``ApplicationContainer``.
That
[container creates our application instance](https://github.com/dherges/ok-testing-reloaded/commit/596edf4d64b0f2bdb3b54aa3e9d90d7baa80654f)
and we change our testing code to work with the container.
We then change ``Twitter App`` so that it needs a ``TweetsDb`` (in other words: ``TwitterApp`` depends on ``TweetsDb``)
and we
[pass that dependency to the constructor of the application instance](https://github.com/dherges/ok-testing-reloaded/commit/21f92541b2db60d239d55ddf376aa2f4915b62e1).
At that point, our application is using dependency injection.
No framework.
No annotations.
No magic.
Just dependency injection.

So that gives us the ability to
[add a fake database in test runs](https://github.com/dherges/ok-testing-reloaded/commit/9dcc45e378eec877e18b74c01d6a0b349538642d).
Beware of the words: we called that class ``MockedTweetsDb`` which suggests that it is a mock.
However, as mentioned above test doubles are either stubs, fakes, or mocks.
In our case, that ``MockedTweetsDB`` is actually a fake and maybe we should have called it ``FakeTweetsDb`` in the
first place.

In order to make our self-made dependency injection re-usable across different projects we can
[extract a generic interface ``SparkApplicationContainer``](https://github.com/dherges/ok-testing-reloaded/commit/9528c04ca7df75c50e3297b25d59b76783222192).
A benefit is that ``SparkApplicationTest`` and ``SparkRunner`` don't need to import classes from our application.
They depend on just that generic ``SparkApplicationContainer`` as a layer of indirection.
Maybe we should've done that in the first place, too.
I apologise for causing confusion.

Either way, what you see:
dependency injection can be super simple if you know how to utilize it.
Why did we do all this?
In test runs, ``TestApplicationContainer`` gives our application a fake database that holds well-known data and we
can write assertions on the response content of our application.
In production, our application should connect to a real database and we achieve that with a
``ProductiveApplicationContainer``.


---


## Summary

When all is said and done, it's time to recall the past and give a summary.

For testing web client applications, we used a mock web server to replay HTTP traffic.
We discussed that this approach will give us extra benefit for end-to-end testing in the future.

For server-side web applications, we used an embedded server to run the application in an environment for integration
testing.
We wrote our integration tests from a consumer's point of view, thus documenting request/response pairs of the
application's HTTP APIs.
We showed that hand-crafted dependency injection is as powerful as a full framework, giving us the ability to substitute
real-world dependencies with test doubles.

In the future, I'd like to build upon those ideas and write more on testing, with a focus on end-to-end and integration
testing respectively.
As mentioned here, I'd like to demonstrate a feature-driven approach that enables to write specification, testing, and
documentation in one go.

Although we pretended very very hard, we still didn't receive any TWTR shares. :-)


## Tech Stack

* A few ok libraries
  * Presented at Droidcon MTL 2015: [Talk](https://youtu.be/WvyScM_S88c), [Slides](https://speakerdeck.com/jakewharton/a-few-ok-libraries-droidcon-mtl-2015)
  * [Retrofit][retrofit]
  * [OkHttp][okhttp]
  * [Moshi][moshi]
  * [Okio][okio]
* [mockwebserver+][mockwebserver], a tool for replaying scripted HTTP traffic
* [AssertJ][assertj], fluent assertions for java
* [Spark][spark], a micro framework with an embedded web server
* [JsonPath][jsonpath], an expression language to crawl through JSON documents
* [Gradle][gradle], build tool for Java applications


[retrofit]: http://square.github.io/retrofit/
[okhttp]: http://square.github.io/okhttp/
[moshi]: https://github.com/square/moshi
[okio]: https://github.com/square/okio
[mockwebserver]: https://github.com/orhanobut/mockwebserverplus
[spark]: http://sparkjava.com/
[assertj]: http://joel-costigliola.github.io/assertj/index.html
[jsonpath]: http://goessner.net/articles/JsonPath/
[jsonpathjayway]: https://github.com/jayway/JsonPath
[dagger]: http://square.github.io/dagger/
[dagger2]: http://google.github.io/dagger/
[gradle]: https://github.com/gradle/gradle
