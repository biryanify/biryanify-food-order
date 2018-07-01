function onFormSubmit(e) {
  var form = FormApp.openById('1Z65umQSZ2drz3MvTWCWlwP4-ySeoB5D7RvX1LtusnEU');
  var formResponses = form.getResponses();
  var latestResponse = formResponses.length - 1;
  var formResponse = formResponses[latestResponse];
  
  var email = formResponse.getRespondentEmail();
  
  var items = formResponse.getItemResponses();
  
  var name = getMethod(items[3]);
  var phone = getMethod(items[4]);
  var flat = getMethod(items[5]);
  var area = getMethod(items[6]);
  var landmark = getMethod(items[7]);
  var item = getMethod(items[0]).toString();
  var quantity = getMethod(items[1]);
  var method = getMethod(items[8]).toString();
  var suggestion = getMethod(items[9]);
  
  var dataToExport = {};
  
  dataToExport[phone] = 
                   {  
                    "name": name,
                    "email":email,
                    "phone":phone,
                    "address":
                     { 
                       "flat":flat,
                       "area":area,
                       "landmark":landmark
                     },
                     "item": item,
                     "quantity": quantity,
                     "method": method,
                     "suggestion": suggestion
                   };

  Logger.log(dataToExport);
  
  var date = Utilities.formatDate(new Date(), "GMT+1", "dd_MM_yyyy");
  
  var finalData = {};
  finalData[date] =  dataToExport;
  
  var base = FirebaseApp.getDatabaseByUrl('https://sendingresponse-64495.firebaseio.com/');
  base.setData("orders", finalData);
}

function getMethod(item) {
  var value = item || "_unknown_";
  
  function check(x) {
    if(x == "")
      return "_unknown_";
    else 
      return x;
  }
  
  if(value != "_unknown_") {
    return check(value.getResponse());
  } else {
    return value;
  }
}


