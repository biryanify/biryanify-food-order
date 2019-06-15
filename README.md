# biryanify-food-order
An app for food delivery management

An android app that takes food order from Google form and lets you manage your inventory from the app. 


### Tech Stack:


Java


Firebase


AppScript


### Writing an AppScript to send Google Form Response to Firebase to an Android App


This is a hack to get an ecommerce website for free. I had to come up with an ecommerce website within a week so I came up with this solution. 


When you are creating/editing your Google form, under the 'meat balls menu (thats how I like to call it) you'll find 'Script Editor'. This is where the script goes. 


You can have a look at the Google Form [here](https://bit.ly/biryanify).


### Function of Script(Sequentially):


Store all the response in a variable


Then the variables are converted into a JSON


The JSON is then sent to my firebase database using AppScript library for Firebase


You can have a look at the script [here](https://gist.github.com/baymac/45bcf98e70b04938154f7ef485aecd25).


#### Firebase:


Firebase is part of Google Cloud Platform that provides a realtime database for web and mobile applications. It stores data as Key-Value pair or JSON format which is similar to NoSQL (not same). So the form responses are sent as JSON object. Setting up firebase for android is very easy, search for firebase in Google.


#### Android:


The functions of the android app in a nutshell:


Everytime firebase receives a form response, the app receives a notification of new order placed. 


On tapping the notification, it opens up app to gives details of the orders such as name, address, email etc


On opening the app you can select the date for which you want to see the orders.


Orders for a date is seen as a list of items, and which individually if tapped on expands into the details of that particular order.
