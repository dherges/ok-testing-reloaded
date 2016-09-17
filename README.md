# ok-testing-reloaded

> Ok, let's go testing HTTP applications with a few ok libraries.



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
That means that we set up a mock web server that replays scripted HTTP responses.

Then, our client talks to the mock web server so that we know the response in advance, allowing us to make assertions
for our client's behaviour.
When should our client return an error code?
How should the response data be represented in Java classes?

Also, we can inspect the recorded request that we mock web server received and we can answer more questions like:
Did our client include the expected request parameters?

Code can be seen at ``TwitterApiTest``.


### Implementing an (almost-)production-ready Twitter client

With our test in place, we can now focus on writing our client.
Since we love Squares's _"ok library stack"_, we chose [Retrofit][retrofit] for our REST/JSON client.

Implementation code can be seen at ``TwitterApi``.
After changing our test code to use the new ``TwitterApi``, we can re-run our tests against the mocked HTTP web server
and will see that our client still works.
That's how it's supposed to be.

We can now start to add missing features like Object to JSON conversion.
When adding such a feature, we enhance our test case to check that the Java objects are returned from a response.

Since we're a little bit tired of repeating the same assertions over and over again, we'd like to save us a few us a
few keystrokes and introduce some [custom assertions](http://joel-costigliola.github.io/assertj/assertj-core-custom-assertions.html).
We add a ``RecordedRequestAssert`` and a ``ResponseAssert`` and change our test case to use these assertions.
Then, we realize that we've exchanged some 5 lines of code in ``TwitterApiTest`` for some 50 lines of code in our
_newly, fancy, shiny_ assertion classes and wait for that investment to pay off in the future.


## Reading List

* A few ok libraries
  * [Retrofit][retrofit]
  * [OkHttp][okhttp]
  * [Moshi][moshi]
  * [Okio][okio]
* [AssertJ][assertj], fluent assertions for java

[retrofit]: http://square.github.io/retrofit/
[okhttp]: http://square.github.io/okhttp/
[moshi]: https://github.com/square/moshi
[okio]: https://github.com/square/okio
[assertj]: http://joel-costigliola.github.io/assertj/index.html
