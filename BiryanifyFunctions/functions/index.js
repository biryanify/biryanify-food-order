'use strict';
const functions = require('firebase-functions');
const admin = require('firebase-admin');
admin.initializeApp();

// // Create and Deploy Your First Cloud Functions
// // https://firebase.google.com/docs/functions/write-firebase-functions
//
// exports.helloWorld = functions.https.onRequest((request, response) => {
//  response.send("Hello from Firebase!");
// });
exports.sendNewOrderNotification =
  functions.database.ref('/orders/{date}/{phone}')
  .onWrite((change, context) => {
    const date = context.params.date;
    const phone = context.params.phone
    console.log('We have a new order on: ', date, ' from phone: ', phone);

    let token = ['eJ1R4N9pTFI:APA91bHdcX3qrLXgwOLNT2jnkTICDbCYS8nSYHMM6IMA0BG4FUPSIXFq3zV9t30LQElAq_B_TnKx5UPijSE2T5ZWFeBwlknz0TDYdRa1Jor4yZ-KZBTxOP9BXUHKNngJZB6OP-uZh5Mp_oYtwcqNkf5eSR5F_BtU9Q']

    return admin.database()
          .ref(`/orders/${date}/${phone}`)
          .once('value')
          .then(snapshot => {
            const payload = {
              "data": {
                "title": 'New order placed!',
                "body": `${snapshot.val().name} has ordered ${snapshot.val().quantity} plates`
              }
            };
            return admin.messaging().sendToDevice(token, payload);
          });
  });
