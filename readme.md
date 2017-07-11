

SimpleHttpAndroid
-------------

A Simpe HTTP client for android and the Java Application.

Maven


    <dependency>
      <groupId>com.mantraideas.simplehttp</groupId>
      <artifactId>simplehttp</artifactId>
      <version>1.0.9</version>
      <type>pom</type>
    </dependency>

Gradle

    compile 'com.mantraideas.simplehttp:simplehttp:1.0.9'

**ProGuard**

If you are using ProGuard you might need to add the following option:

    -keep class com.mantraideas.simplehttp.datamanager.** {*;}
    -keep class com.mantraideas.simplehttp.datamanager.DataRequestManager {*;}
    -dontwarn com.mantraideas.simplehttp.datamanager.**
    -dontwarn com.mantraideas.simplehttp.datamanager.DataRequestManager 

**Support**
> Currently it supports the GET, POST, PUT and DELETE request method

**Making the Request**
> Passing the form data in the body

    DataRequestPair requestPair = DataRequestPair.create();
            requestPair.put("password", "12345");
            requestPair.put("email", "test@tests.com");


> Passing the JSONObject in the body

     String sampleJSONbody = "{\n" +
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
            DataRequestPair requestPair = DataRequestPair.create(sampleJSONbody);

> **Now creating the request**

     DataRequest request = DataRequest.getInstance()
                    .addHeaders(new String[]{"your_header_key"}, new String[]{"your_header_value"});
            // replace this with your domain to test
            request.addUrl("https://www.yourdomain.com");
            request.addDataRequestPair(requestPair);
            request.addMethod(Method.POST);
for GET

    request.addMethod(Method.GET);

Similarly for other request aswell.

Others supported function

    request.addMinimumServerCallTimeDifference(2000);
This will add the minimum server call time so that server will be called after 2 seconds only after the first call.

**Finally**
Execute the request

     DataRequestManager<String> requestManager = DataRequestManager.getInstance(getApplicationContext(), String.class);
            requestManager.addRequestBody(request).addOnDataRecieveListner(new OnDataRecievedListener() {
                @Override
                public void onDataRecieved(Response response, Object object) {
                    Log.d("test", " data from server = " + object.toString());
                }
            }, new OnDataRecievedProgressListener() {
                @Override
                public void onDataRecievedProgress(int completedPercentage) {
                    Log.d("MainActivity", "Progress = " + completedPercentage);
                }
            });
            requestManager.sync();

> Currently OnDataRecieveProgressListener interface only works in GET request.

Data Recieved as Object. The recieved object depends on the parameters send while creating the instance of the DataRequestManager. For eg


    DataRequestManager<Profile> requestManager = DataRequestManager.getInstance(getApplicationContext(),
                    Profile.class);
This will gives the instance of Profile in OnDataRecieveListener CallBack.

     requestManager.addRequestBody(request).addOnDataRecieveListner(new OnDataRecievedListener() {
                @Override
                public void onDataRecieved(Response response, Object object) {
                    // since the json Array is recieved from the get Request this library gives the list of the class passed as parameter
                    // here Profile is passed hence the object will be the list of profile.
                    // if it is jsonobject it will give the pofile object
                    if (response == Response.OK) {
                        Log.d("MainActivity", "data = " + object.toString());
                        List<Profile> profileList = (List<Profile>) object;
                        Log.d("MainActivity", "list size = " + profileList.size());
                    } else {
                        Log.d("MainActivity", "response = " + response.getMessage());
                    }
                }
            });
            requestManager.sync();

If the response from the RestAPI is JSONArray then it will return the List of that object and instance of the object for the JSONObject.

**Example**
Lets take the Sample API, This will give the JSONObject.
http://github.yubrajpoudel.com.np/others/sample1.json

Create Profile Class "Profile.java"

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
> Please put the empty constructor in the Model class otherwise it will throw error.
Now Doing Request, we will obtain the data from server as the object of Profile

    // this is optional, mulitple header can be passed by keeping it in array
    DataRequest request = DataRequest.getInstance();
    // request.addHeaders(new String[]{"your_header_key"}, new String[]{"your_header_value"});
    request.addUrl("http://github.yubrajpoudel.com.np/others/sample2.json");
    request.addMethod(Method.GET);
    DataRequestManager<Profile> requestManager = DataRequestManager.getInstance(getApplicationContext(),
            Profile.class);
    requestManager.addRequestBody(request).addOnDataRecieveListner(new OnDataRecievedListener() {
        @Override
        public void onDataRecieved(Response response, Object object) {
            // since the json Array is recieved from the get Request this library gives the list of the class passed as parameter
            // here Profile is passed hence the object will be the list of profile.
            // if it is jsonobject it will give the pofile object
            if (response == Response.OK) {
                Profile profile = (Profile) object;
                Log.d("MainActivity", "Profile name = " + profile.name + " address = " + profile.address + " profession = " + profile.profession);
            } else {
                Log.d("MainActivity", "response = " + response.getMessage());
            }
        }
    }, new OnDataRecievedProgressListener() {
        @Override
        public void onDataRecievedProgress(int completedPercentage) {
            Log.d("MainActiivity", "completedPercentage = " + completedPercentage);
        }
    });
    requestManager.sync();

Yeah Thats all.
For more information please see out the sample and for any queries or suggestion mail me at "yubaraj@mantraideas.com". 
