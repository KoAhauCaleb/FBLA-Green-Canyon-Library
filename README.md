# Green Canyon Library

### Features
##### Working:
  - View books in library including cover, description, and author.
  - Login by scanning your school ID card or typing in your school ID.
  - 
##### Planned:
  - Check out and hold books.
  - View holds and due date.
  - Get notifications for upcoming due dates and if a held book is available.
  
### Technical Details:
Green Canyon Library uses AWS (Amazon web services) RDS to store the info for each book in an MySQL database. The app receives this data by sending a request to an AWS lambda function. The functions return a JSON string which contains the info for the requested books. Included in this string is a URL to an image of the cover of the book. The app uses the Piccasso library to download and cache this image.

### Libraries
Green Canyon Library uses a couple different librarys:
  - Zxing - Read barcodes
  - Piccasso - Download images and resize them corectly

### Installation
You can install and run Green Canyon Library on an android device by:
  - Installing and or opening Android Studio.
  - If a project is already open click file > close project
  - On the welcome page click check out from version control > github
  - in the URL field enter "https://github.com/gtwogtwo/Green-Canyon-Library.git".
  - Follow the directions onscreen to clone and open it.
  - Once done enable Android debuging in the develper options on your phone, which you can useually access by taping on settings>about>build number7 times, and then plug into computer.
  - press run and wait for it to open on android device.

