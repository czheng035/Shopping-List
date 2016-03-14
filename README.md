# Shopping List

The shopping list app completes the following requirements:
1. A field where the user specifies their “id”. The shopping list items saved
will be paired to the id specified.
2. A “load list” button that displays previously saved lists for a user of the
particular id.
3. A page that lists all ids of users.
4. A text field where the user inputs what they want to added to the list. This list should be available even when the user terminates and relaunches the app.
5. Make sure that the information saved is specific to this user.
6. Every entry in the list should contain a “Done” which removes the item
when the user clicks it.

## Extra Features
- When users input a user id, shopping list name or item, the displaying list makes a responsive simple search
- The done button is shown when users swipe left the corresponding list item. A strike-through marks an item has been bought.

## Demo
Just down load debug [apk](https://github.com/czheng035/Shopping-List/blob/master/app-debug.apk) and install it on your phone, API level 16-23 support.

![Demo](https://github.com/czheng035/Shopping-List/blob/master/demo.gif)

## Data Model
![Data Model](https://github.com/czheng035/Shopping-List/blob/master/data-model.png)

## Test
The project provides a content provider test with a few test cases at [Test Link](https://github.com/czheng035/Shopping-List/tree/master/app/src/androidTest/java/com/knowroaming/czheng035/shoppinglist/data).

## Third-party library
The project contains a [library module](https://github.com/daimajia/AndroidSwipeLayout) for the swipe-to-delete action. Their jcenter() dependency seems out-of-date and buggy so I import the whole source project.