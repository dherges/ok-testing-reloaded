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
