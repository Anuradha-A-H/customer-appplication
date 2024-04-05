![img1 login](https://github.com/Anuradha-A-H/customer-appplication/assets/119663653/a19554eb-2437-48d7-b4e7-d343ee4eaaf4)
![Screenshot 2024-04-05 143238](https://github.com/Anuradha-A-H/customer-appplication/assets/119663653/ad1ef898-e1e8-4f6a-a284-592cc2c69cc1)

1st phase:
1) login is created, 1st time when there is not data fst data is considered as admin (as the question specified only 3 view pages and no hardcoding the credentials so I have added this logic)
   token is generated once login(JWT  and role based authorization is done). If customer try to login it throw error as customer is not allowed to edit or view all customer
3) If login credential is wrong it will throw error else redirect to customer listing page( listing is viewed only my admin)
 ![img2 add customer](https://github.com/Anuradha-A-H/customer-appplication/assets/119663653/0068bd6e-156c-48a3-8a00-92423791310e)
4) add customer when click of add customer button// it redirect to add customer page and add customer details with customer role by default
5) once added successfully redirect to list customer page with customer credentials or error message(as the question specified only 3 view pages so this add customer page also act add update customer)

      ![img3 sync upload](https://github.com/Anuradha-A-H/customer-appplication/assets/119663653/17c58550-8283-4835-a07c-c328674faaa4)
6) on click of sync button call the remote API which return the token back using that token call list remote APT. validation are added to it - if data already present update that customer, check email format whether it ends with @gmail and check phone number is in number format and email not empty .

   
![img4 search filter](https://github.com/Anuradha-A-H/customer-appplication/assets/119663653/f03d72c8-bb44-4daf-b037-f2af7bde46dc)
7) search dropdown is made for Firstname, City, Email, Phone number with pagination
 ![img5 update customer](https://github.com/Anuradha-A-H/customer-appplication/assets/119663653/c5fd4f61-0514-4a1e-83c8-06f66585eb23)
8) on click of edit icon it redirect to  edit customer in add customer page add edit the customer, redirects with success message to customer list page , if error error displayed in same page. Feild required validation is added in front end and also validate phone number(as the question specified only 3 view pages add customer page work as edit and also add)

9) delete customer on click of delet icon



   

