# Project 1 - *Porygon*

**Poorygon** is an android app build to clone some of the features users have in the Twitter app, by using the Twitter API.

Submitted by: **Amari Garrett**

Time spent: **48** hours spent in total

## User Stories

The following **required** functionality is completed:

* [X] User can sign in to Twitter using OAuth login
* [X] User can view the tweets from their home timeline
* [X] User can compose a new tweet
* [X] While composing a tweet, user can see a character counter with characters remaining for tweet out of 280
* [X] User can refresh timeline of tweets by pulling down to refresh
* [X] User can see embedded image media within a tweet on list view (required) and detail view (stretch, optional)


The following **stretch** features are implemented:

* [X] Improve the user interface and theme the app to feel "twitter branded" with colors and styles
* [X] When any background or network task is happening, user sees an indeterminate progress indicator
* [X] User can click on a tweet to be taken to a "detail view" of that tweet
* [X] User can view more tweets as they scroll with Endless Scrolling. Number of tweets is unlimited.
* [X] Replace all icon drawables and other static image assets with vector drawables where appropriate.
* [X] Apply the View Binding library to reduce view boilerplate.
* [X] User can open the twitter app offline and see last loaded tweets persisted into SQLite

## Video Walkthrough

Here's a walkthrough of implemented user stories:

**Infinite Scroll**

<img src='https://github.com/Amari-G/codepath/blob/master/Kap/inf_scroll.gif' />

**Compose Tweet**

<img src='https://github.com/Amari-G/codepath/blob/master/Kap/compose.gif' />

**Persistence**

<img src='https://github.com/Amari-G/codepath/blob/master/Kap/persistence.gif' />

GIFs created with [Kap](http://www.getkap.co/).

## Open-source libraries used
* [scribe-java](https://github.com/fernandezpablo85/scribe-java) - Simple OAuth library for handling the authentication flow.
* [Android Async HTTP](https://github.com/codepath/AsyncHttpClient) - Simple asynchronous HTTP requests with JSON parsing
* [codepath-oauth](https://github.com/thecodepath/android-oauth-handler) - Custom-built library for managing OAuth authentication and signing of requests
* [Glide](https://github.com/bumptech/glide) - Used for async image loading and caching them in memory and on disk.
* [Room](https://developer.android.com/training/data-storage/room/index.html) - Simple ORM for persisting a local SQLite database on the Android device
