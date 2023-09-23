use master;
go
drop database DuAn2;
go
create database	DuAn2;
go
use DuAn2;
go
Create table Account
(
	Account_ID		int				identity(1,1)		primary key,
	Name			nvarchar(50)	not null			CHECK (LEN(Name) <= 50),
	Password		nvarchar(50)	not null,
	Address			nvarchar(255)	null				CHECK (LEN(Address) <= 255),
	Phone			varchar(13)		null				unique,
	Email			varchar(50)		not null			unique			CHECK (Email like '%_@_%._%'),
	Role			bit				not null			default(1),
	Active			bit				not null			default(1)
);
go
Create table [Order]
(
	Order_ID		int				identity(1,1)		primary key,
	Order_Status	int				not null			default(0),
	Order_Date		datetime2		not null,
	shipping_Fee	decimal(18,2)	not null			default(20000),
	discount		decimal(18,2)	not null			default(0),
	Total_Price		decimal(18, 2)	not null,
	AccountOrder_ID	int				references [Account](Account_ID)
);
go
CREATE TABLE [Category]
(
    Category_ID     int             identity(1,1)      primary key,
    Category_Name   nvarchar(50)    not null
);
GO

Create table [Product]
(
	Product_ID		int				identity(1,1)		primary key,
	Product_image	varchar(255)	not null,
	Product_Name	nvarchar(50)	not null,
	Product_Price	decimal(18, 2)	not null,
	Product_Description		nvarchar(255)				not null,
	Product_quantity		int		not null			default(-1),
	isPopular				bit		not null			default(0),
	Product_Active			bit		not null			default(1),
	Product_Creation_Date	datetime2	not null,
	ProductCategory_ID		int,
	FOREIGN KEY (ProductCategory_ID) REFERENCES [Category](Category_ID)
);
go
Create table	Favorite_Product
(
	Favorite_Product_ID		int				identity(1,1)		primary key,
	FK_accountID			int				REFERENCES	[Account](Account_ID),	
	FK_ProductID			int				REFERENCES	[Product](Product_ID)
)
go
Create table [Detail_Order]
(
	Detail_Order_ID		int				identity(1,1)		primary key,
	Quantity			int				not null			default(1),
	Sub_Total			decimal(18, 2)	not null,
	FK_Order_ID			int				REFERENCES [Order](Order_ID), 
	FK_Product_ID		int				REFERENCES [Product](Product_ID)
);
GO
Create table FeedBack
(
	FeedBack_ID		int				identity(1,1)		primary key,
	Rate			int				not null,
	Comment nvarchar(255)			not null			CHECK (LEN(Comment) <= 255),
	Creation_Date	datetime2		not null,
	Active			bit				not null			default(1),
	FeedBack_Product_ID				int					REFERENCES Product(Product_ID), 
	FeedBack_Account_ID				int					REFERENCES Account(Account_ID)
);
go
CREATE TABLE VerificationCode (
    id INT IDENTITY(1,1) PRIMARY KEY,
    email NVARCHAR(255) NOT NULL,
    code NVARCHAR(255) NOT NULL,
    expiration_date DATETIME NOT NULL
)
go
INSERT INTO Account (Name, Password, Address, Phone, Email, Role, Active)
VALUES
    ('John Doe', 'password1', '123 Main Street, New York', '123-456-7890', 'john@example.com', 0, 1),
    ('Jane Smith', 'password2', '456 Elm Street, Los Angeles', '987-654-3210', 'jane@example.com', 1, 1),
    ('Michael Johnson', 'password3', '789 Oak Avenue, Chicago', '555-123-4567', 'michael@example.com', 0, 1),
    ('Emily Davis', 'password4', '101 Pine Road, San Francisco', '777-888-9999', 'emily@example.com', 1, 1),
    ('William Brown', 'password5', '555 Maple Lane, Boston', '222-333-4444', 'william@example.com', 0, 1),
    ('Sophia Wilson', 'password6', '222 Cedar Street, Miami', '999-000-1111', 'sophia@example.com', 1, 1),
    ('Ethan Lee', 'password7', '777 Birch Avenue, Seattle', '111-222-3333', 'ethan@example.com', 0, 1),
    ('Olivia White', 'password8', '333 Redwood Road, Denver', '444-555-6666', 'olivia@example.com', 1, 1),
    ('James Martinez', 'password9', '888 Willow Lane, Atlanta', '777-666-5555', 'james@example.com', 0, 1),
    ('Ava Adams', 'password10', '444 Sycamore Drive, Dallas', '222-111-0000', 'ava@example.com', 1, 1);

	go
-- Thêm dữ liệu vào bảng Category
INSERT INTO [Category] (Category_Name)
VALUES ('tea'), ('coffee'), ('food'), ('package');
	go
	-- Thêm 20 dữ liệu vào bảng Product với dữ liệu mẫu
INSERT INTO Product (Product_image, Product_Name, Product_Price, Product_Description, Product_quantity, isPopular, Product_Active, Product_Creation_Date, ProductCategory_ID)
VALUES
    ('product1.jpg', 'Green Tea', 5.99, 'A pack of green tea', 100, 1, 1, GETDATE(), 1),
    ('product2.jpg', 'Black Coffee', 4.99, 'A cup of black coffee', 200, 0, 1, GETDATE(), 2),
    ('product3.jpg', 'Biscuits', 2.49, 'A pack of biscuits', 150, 1, 1, GETDATE(), 3),
    ('product4.jpg', 'Earl Grey Tea', 6.99, 'A pack of Earl Grey tea', 80, 1, 1, GETDATE(), 1),
    ('product5.jpg', 'Cappuccino', 5.49, 'A cup of cappuccino', 180, 0, 1, GETDATE(), 2),
    ('product6.jpg', 'Chocolate Chip Cookies', 3.99, 'A pack of cookies', 120, 1, 1, GETDATE(), 3),
    ('product7.jpg', 'Chamomile Tea', 6.49, 'A pack of chamomile tea', 90, 1, 1, GETDATE(), 1),
    ('product8.jpg', 'Latte', 4.79, 'A cup of latte', 160, 0, 1, GETDATE(), 2),
    ('product9.jpg', 'Oatmeal Cookies', 3.49, 'A pack of oatmeal cookies', 130, 1, 1, GETDATE(), 3),
    ('product10.jpg', 'Hibiscus Tea', 6.79, 'A pack of hibiscus tea', 70, 1, 1, GETDATE(), 1),
    ('product11.jpg', 'Espresso', 4.29, 'A cup of espresso', 140, 0, 1, GETDATE(), 2),
    ('product12.jpg', 'Snickerdoodle Cookies', 3.69, 'A pack of snickerdoodle cookies', 110, 1, 1, GETDATE(), 3),
    ('product13.jpg', 'Peppermint Tea', 6.99, 'A pack of peppermint tea', 60, 1, 1, GETDATE(), 1),
    ('product14.jpg', 'Mocha', 5.69, 'A cup of mocha', 120, 0, 1, GETDATE(), 2),
    ('product15.jpg', 'Peanut Butter Cookies', 3.49, 'A pack of peanut butter cookies', 100, 1, 1, GETDATE(), 3),
    ('product16.jpg', 'Chai Tea', 7.29, 'A pack of chai tea', 50, 1, 1, GETDATE(), 1),
    ('product17.jpg', 'Iced Coffee', 4.99, 'A cup of iced coffee', 110, 0, 1, GETDATE(), 2),
    ('product18.jpg', 'Shortbread Cookies', 3.99, 'A pack of shortbread cookies', 90, 1, 1, GETDATE(), 3),
    ('product19.jpg', 'Ginger Tea', 7.49, 'A pack of ginger tea', 40, 1, 1, GETDATE(), 1),
    ('product20.jpg', 'Caramel Macchiato', 5.99, 'A cup of caramel macchiato', 100, 0, 1, GETDATE(), 2);
go

INSERT INTO Favorite_Product (FK_accountID, FK_ProductID)
VALUES
    (1, 1),
    (1, 2),
    (2, 3),
    (3, 4),
    (4, 5);
go
select * from Account
go
select * from [Order]
go
select * from [Category]
go
select * from [Product]
go
select * from Favorite_Product
go
select * from [Detail_Order]
go
select * from FeedBack
go