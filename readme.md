

SimpleHttpAndroid
-------------

A Simpe HTTP client for android and the Java Application.

Maven


    <dependency>
      <groupId>com.mantraideas.simplehttp</groupId>
      <artifactId>simplehttp</artifactId>
      <version>1.0.1</version>
      <type>pom</type>
    </dependency>

Gradle

    compile 'com.mantraideas.simplehttp:simplehttp:1.0.2'

**ProGuard**

If you are using ProGuard you might need to add the following option:
> -dontwarn com.mantraideas.simplehttp.datamanager.** 


**GET Request**
=============

>  

 

     DataRequest request = DataRequest.getInstance().addHeaders(new String[]{"your_header_key"}, new String[]{"your_header_value"});
        request.addUrl("https://www.yourdomain.com");
        request.addMethod(Method.GET);
        DataRequestManager<String> requestManager = DataRequestManager.getInstance(getApplicationContext(), String.class);
        requestManager.addRequestBody(request).addOnDataReciveListner(new OnDataRecievedListener() {
            @Override
            public void onDataRecieved(Response response, Object object) {
                Log.d("test", " data from server = " + object.toString());
            }
        });
        requestManager.getData();
**To get the data in either List or Object**
let create the sample model for example in this example i have created the *Profile.java*
> 

    // Sample class
        public class Profile {
            public String name, address, profession;
              public Profile(JSONObject jsonObject) {
                this.name = jsonObject.optString("name");
                this.address = jsonObject.optString("address");
                this.profession = jsonObject.optString("profession");
                }
            public Profile(){   
            }
        }
       
> Note : Please put the empty constructor always in the model.
       
**Response from server** "http://github.yubrajpoudel.com.np/others/sample1.json"

![enter image description here](https://github.com/yuviii/SimpleHttpAndroid/blob/master/response.png?raw=true)
    
*Since the response from server is JSONArray this will itself gives the List of objects of the type that passed as the parameter in the DataRequestManager in OnDataRecievedListener Callback

>  for example, to get list of profile class  

    DataRequestManager<Profile> requestManager = DataRequestManager.getInstance(getApplicationContext(),Profile.class);

Full code

     public void doGetMethodOfRestAPIForJSONArray() {
            Log.d("test", "started checking get test");
            // this is optional, mulitple header can be passed by keeping it in array
            DataRequest request = DataRequest.getInstance();
            // request.addHeaders(new String[]{"your_header_key"}, new String[]{"your_header_value"});
            request.addUrl("http://github.yubrajpoudel.com.np/others/sample1.json");
            request.addMethod(Method.GET);
            DataRequestManager<Profile> requestManager = DataRequestManager.getInstance(getApplicationContext(),
                    Profile.class);
            requestManager.addRequestBody(request).addOnDataReciveListner(new OnDataRecievedListener() {
                @Override
                public void onDataRecieved(Response response, Object object) {
                    // since the json Array is recieved from the get Request this library gives the list of the class passed as parameter
                    // here Profile is passed hence the object will be the list of profile.
                    // if it is jsonobject it will give the pofile object
                    if (response == Response.OK) {
                        List<Profile> profileList = (List<Profile>) object;
                        Log.d("MainActivity", "list size = " + profileList.size());
                    } else {
                        Log.d("MainActivity", "response = " + response.getMessage());
                    }
                }
            });
            requestManager.getData();
        }

If request gives the JsonObject then it will give the instance of  class you have passed itself.

    public void doGetMethodOfRestAPIForJSONObject() {
            Log.d("test", "started checking get test");
            // this is optional, mulitple header can be passed by keeping it in array
            DataRequest request = DataRequest.getInstance();
            // request.addHeaders(new String[]{"your_header_key"}, new String[]{"your_header_value"});
            request.addUrl("http://github.yubrajpoudel.com.np/others/sample2.json");
            request.addMethod(Method.GET);
            DataRequestManager<Profile> requestManager = DataRequestManager.getInstance(getApplicationContext(),
                    Profile.class);
            requestManager.addRequestBody(request).addOnDataReciveListner(new OnDataRecievedListener() {
                @Override
                public void onDataRecieved(Response response, Object object) {
                    // since the json Array is recieved from the get Request this library gives the list of the class passed as parameter
                    // here Profile is passed hence the object will be the list of profile.
                    // if it is jsonobject it will give the pofile object
                    if (response == Response.OK) {
                        Profile profile = (Profile)object;
                        Log.d("MainActivity", "Profile name = " + profile.name + " address = " + profile.address + " profession = " + profile.profession);
                    } else {
                        Log.d("MainActivity", "response = " + response.getMessage());
                    }
                }
            });
            requestManager.getData();
        }

So just use the object received from the Server straight away. Yeaahhhh !!!!

**POST Request**
===========

*Can do the the post request by either passing the form data or through  by body with JSONObject*

By passing the form-data

     public void doPostMethodOfRestAPIWithFormData() {
            final Response expected = Response.OK;
            DataRequestPair requestPair = DataRequestPair.create();
            requestPair.put("password", "12345");
            requestPair.put("email", "test@tests.com");
    
            // mulitple header can be passed by keeping it in array
            DataRequest request = DataRequest.getInstance().addHeaders(new String[]{"your_header_key"}, new String[]{"your_header_value"});
            request.addUrl("https://www.yourdomain.com/api/rest/login");
            request.addDataRequestPair(requestPair);
            request.addMethod(Method.POST);
            DataRequestManager<String> requestManager = DataRequestManager.getInstance(getApplicationContext(), String.class);
            requestManager.addRequestBody(request).addOnDataReciveListner(new OnDataRecievedListener() {
                @Override
                public void onDataRecieved(Response response, Object object) {
                    Log.d("test", " data from server = " + object.toString());
                    Assert.assertEquals(response.getMessage(), expected, response);
                }
            });
            requestManager.getData();
        }

Or by passing JsonObject in the body 

     public void doPostMethodOfRestApiWithJSONBody() {
            final Response expected = Response.OK;
            String samplebody = "{\n" +
                    "\t\"address_1\": \"Test street 88\",\n" +
                    "\t\"address_2\": \"test\",\n" +
                    "\t\"city\": \"Berlin\",\n" +
                    "\t\"company_id\": \"company\",\n" +
                    "\t\"company\": \"company\",\n" +
                    "\t\"country_id\": \"97\",\n" +
                    "\t\"email\": \"test@test.com\",\n" +
                    "\t\"fax\": \"1\",\n" +
                    "\t\"firstname\": \"Test\",\n" +
                    "\t\"lastname\": \"User\",\n" +
                    "\t\"postcode\": \"1111\",\n" +
                    "\t\"tax_id\": \"1\",\n" +
                    "\t\"telephone\": \"+36306668884\",\n" +
                    "\t\"zone_id\": \"1433\",\n" +
                    "\t\"password\": \"12345\",\n" +
                    "\t\"confirm\": \"12345\",\n" +
                    "\t\"agree\": \"1\"\n" +
                    "}";
            DataRequestPair requestPair = DataRequestPair.create(samplebody);
            DataRequest request = DataRequest.getInstance()
                    .addHeaders(new String[]{"your_header_key"}, new String[]{"your_header_value"});
    
            // replace this with your domain to test
            request.addUrl("https://www.yourdomain.com");
            request.addDataRequestPair(requestPair);
            request.addMethod(Method.POST);
            DataRequestManager<String> requestManager = DataRequestManager.getInstance(getApplicationContext(), String.class);
            requestManager.addRequestBody(request).addOnDataReciveListner(new OnDataRecievedListener() {
                @Override
                public void onDataRecieved(Response response, Object object) {
                    Log.d("test", " data from server = " + object.toString());
                    Assert.assertEquals(response.getMessage(), expected, response);
                }
            });
            requestManager.getData();
        }

Yeah Thats all.
For more information please see out the sample and for any queries or suggestion mail me at "yubaraj@mantraideas.com". 
