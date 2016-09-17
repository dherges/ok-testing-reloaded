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
That means that we set up a [mock web server that replays scripted HTTP responses][mockwebserver].

Then, our client talks to the mock web server so that we know the response in advance, allowing us to make assertions
for our client's behaviour.
When should our client return an error code?
How should the response data be represented in Java classes?

Also, we can inspect the recorded request that we mock web server received and we can answer more questions like:
Did our client include the expected request parameters?

Code can be found in ``TwitterApiTest``.
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


### Testing for behaviour on error codes, network delays, and these kind of things

Let's tweet and tell the world what music we're listening to – right now at that very moment in time.

In ``TwitterApiTest``, we add two more test methods: ``tweet_success()`` and ``tweet_duplicate()``.
In the success case, we let mock web server replay a ``200 Ok`` response with a JSON object of a tweet.

Let's go another round of tweeting.
Twitter says that ["any attempt that would result in duplication will be blocked, resulting in a 403 error."](https://dev.twitter.com/rest/reference/post/statuses/update).
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

With the ability to replay scripted response we can mimic both cases.
We can then write tests that verify application behaviour on success, on a specific error, on a delayed response, and
many more.
End-to-end-testing is a topic of its own that needs an extra article to talk about in depth.


## Testing client applications: skin and bones

With the code that we've written so far we've got a working set up to test a Twitter client.
We feed HTTP responses and JSON documents to the client, putting us in a position where we can dive into the sparkling
times ahead.
It's times like these, you see some benefits again.

Speaking in terms of developer experience, it makes it easy for you to add more features, talk to new API endpoints,
or check if methods behave in the way that they're expected to be.

You've got a little bit of extra confidence that networking, speaking the HTTP protocol, and JSON serialization are
working and they are playing well together.
By chance, these are usually the little things that need to fit together when sound turns into music.

On the edge to outer space, for zealous or academic reasons or even both of them, we need to include the argument:
you could decide to exchange the implementation.
If you do so, remove all the application code, throw away ``TwitterApi``, no longer use Retrofit framework, and let the
tests survive.
They are still there.
Use them to write yet another other Twitter client.

On the back side of the moon, you could grumble that these tests are doing _too much_.
Yes, they are testing library code.
Yes, they are testing integrations of multiple parts.
Is it still what they call a _unit_ test?

Going on from here, take the code as a skeleton for scaling up.
See it as a source of inspiration or just a bunch of fluky ideas – up to how you perceive it.


## Implementing Twitter REST APIs

So far, we've pretended that we're writing a Twitter client.
Now, let's pretend that we've got a job offer from San Francisco and accepted to get paid in
[$TWTR](https://investor.twitterinc.com/stockquote.cfm) common stock.
Not only we're hoping for Twitter to turn into the green (or Google to pay us out), but we're now focused on
implementing API endpoints on the server side.


TODO: next time around ... time and time again :-)



## References

* A few ok libraries
  * [Retrofit][retrofit]
  * [OkHttp][okhttp]
  * [Moshi][moshi]
  * [Okio][okio]
* [AssertJ][assertj], fluent assertions for java
* [mockwebserver+][mockwebserver]


[retrofit]: http://square.github.io/retrofit/
[okhttp]: http://square.github.io/okhttp/
[moshi]: https://github.com/square/moshi
[okio]: https://github.com/square/okio
[mockwebserver]: https://github.com/orhanobut/mockwebserverplus
[assertj]: http://joel-costigliola.github.io/assertj/index.html
