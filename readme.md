# TavernCrawler

## Table of Contents
1. [Overview](#Overview)
1. [Product Spec](#Product-Spec)
1. [Wireframes](#Wireframes)
2. [Schema](#Schema)

## Overview
### Description
TavernCrawler is an app geared towards making the boring side of planning of a bar crawl into a quick and easy endeavour! Instead of having to look up bars one by one and tediously plan out an expedition, you instead have a list of all the bars near you, complete with contact information, menus, a map, and more!

### App Evaluation
- **Category:** Travel & Local 
- **Mobile:** The app would be best designed for mobile devices, as the ability to take information with you and potentially order a ride home would require a mobile device.
- **Story:** Provides a list of local bars, and allows users to plan a route of those bars. Users can then decide to leave a review/rating of those bars.
- **Market:** Only users of legal drinking age can use this app, potentially restricting signing up without verifying age. Geared towards alcohol enthusiasts.
- **Habit:** The app would be designed to provide a convenient source of information for going out to drink, even at just one location. Users would use the app as often as they tend to go drinking.
- **Scope:** Initially, we would concentrate on making sure that the app functions in most urban cities in the USA. This app could broaden its scope eventually to include more areas, with a more comprehensive set of bars, and with closer collaboration with major bars and chains.

## Product Spec

### 1. User Stories (Required and Optional)

**Required Must-have Stories**

* Users can sign in or create account
* User can view a list of local pubs
* User can create a list or route of pubs they want to visit
* Users can rate bars
* Detail screen for bars with a menu, ratings, and contact information
* User can review bars
* User can favorite bars, and view a list of favorites

**Optional Nice-to-have Stories**

* Lyft connectivity to order a ride home to a specified location
* Show deals for local bars (if possible)
* User can blacklist bars

### 2. Screen Archetypes

* Login screen
   * Allow users to sign in to account
   * Connects to similarly designed Registration screen, which allows users to create an account
* Map screen
   * Shows local bars on a map, can select bars to view details
   * Can pin a location as home for Lyft rides
* Details screen
   * Shows details of a specific selected bar
   * Has menu, ratings, contact information
   * Option to favorite or blacklist
* Review screen
   * Screen for filling out and submitting reviews of a specific bar
* List screen
   * List of local bars and distances
   * Can select bars from list to view details or view on map
   * Alternate tabs to view list of favorites and blacklist, can remove favorites or blacklisted bars from these lists
* Nearby offers screen (optional)
   * Shows list of deals and offers for nearby bars 
   * Can select bars to go straight to detail screen or view location on map
* Settings screen
   * User can change several settings regarding the app or their account, or log out and be returned to the login screen

### 3. Navigation

**Tab Navigation** (Tab to Screen)

* Map
* Lists
* Settings

Optional:
* Nearby Offers

**Flow Navigation** (Screen to Screen)

* Login/Account creation screens
   * Leads to map or list screen upon successful login
* Map screen
   * Can switch tab to lists or settings
   * Tap on a location to switch to details
* List screen
   * Can switch tab to map or settings
   * Tap on a location to switch to details
* Settings screen
   * Can switch tab to map or lists
   * Can log out of account, returning to login screen
* Details screen
   * Can back out to previous list or map screen
   * Can enter review screen for writing reviews

## Wireframes
<img src="https://i.imgur.com/LFVLKES.png" width=600>

### [BONUS] Digital Wireframes & Mockups
<img src="https://i.imgur.com/XC088pF.png" width=600>

### [BONUS] Interactive Prototype
![Video Walkthrough](DEMO2.gif)

## Schema 
[This section will be completed in Unit 9]
### Models
Property | Type | Description
--- | --- | ---
objectId | String | Unique identifier for the object (Default)
username | String | Username of the user
password | String | Password of the user
homeAddress | String | Home address of the user
title | String | Title of the user's review
ratingNum | Number | The rating number associated with the review
body | String | Content of the user's review
user | Pointer | User associated with the review
bar | Pointer | Bar that the user has favorited
user | Pointer | User that has favorited the bar
name | String | Name of the bar
location | String | Location of the bar (Multiple of the same bar could exist in different cities)
image | File | Image associated with the bar
### Networking
```java
	ParseQuery<Favorites> query = ParseQuery.getQuery(Favorites.class);
	query.include(currentUser);
        query.addDescendingOrder(reatedAt);
        query.findInBackground(new FindCallback<>() {
            @Override
            public void done(List<Favorites> favorites, ParseException e) {
		if(e != null) Log.e("CLASS_TAG", String.valueOf(e) + "has occurred.", e);
                for(Favorite favorite : favorites) {
                    //Display favorites
                }
                //Do more things with favorites
            }
        });
```
- [Add list of network requests by screen ]
- Home screen page
- Read(Get) | You will get a query of bars in the area
- Delete | Add a bar to a blacklist, which removes from query
- (Create/POST) | Add a like to a bar to the favorites
- Login Screen
- (Create/POST) | Add a username and password to create a new account
- Read(Get) |Get the login information access for the account 
- Map
- Read(Get) | Get an map with an a waypoint to the bar you want to go on the list
- Details Page
- (Create/POST) | CCreate a post of a review
- Read(Get) | Get the infor for the bar including a adresss and get reviews
- Delete | Delete a review post on the bar

- [Create basic snippets for each Parse network request]
- [OPTIONAL: List endpoints if using existing API such as Yelp]
