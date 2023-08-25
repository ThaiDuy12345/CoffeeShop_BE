-- create database	DuAn2;
-- go
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
	Role			bit				not null			default(1)
);
go
Create table [Order]
(
	Order_ID		int				identity(1,1)		primary key,
	Order_Status	int				not null			default(0),
	Order_Date		datetime2		not null,
	Total_Price		decimal(18, 2)	not null,
	AccountOrder_ID	int				references [Account](Account_ID)
);
go
Create table [Category]
(
	Category_ID		int				identity(1,1)		primary key,
	Category_Name	nvarchar(50)	not null
);
go
Create table [Products]
(
	Product_ID		int				identity(1,1)		primary key,
	Product_image	varchar(255)	not null,
	Product_Name	nvarchar(50)	not null,
	Product_Price	decimal(18, 2)	not null,
	Product_Description		nvarchar(255)				not null,
	ProductCategory_ID		int,
	FOREIGN KEY (ProductCategory_ID) REFERENCES [Category](Category_ID)
);
go
Create table [Detail_Order]
(
	Detail_Order_ID		int				identity(1,1)		primary key,
	Quantity			int				not null			default(1),
	Sub_Total			decimal(18, 2)	not null,
	FK_Order_ID			int				REFERENCES [Order](Order_ID), 
	FK_Product_ID		int				REFERENCES [Products](Product_ID)
);
GO
Create table FeedBack
(
	FeedBack_ID		int				identity(1,1)		primary key,
	Rate			int				not null,
	Comment nvarchar(255)			not null			CHECK (LEN(Comment) <= 255),
	Creation_Date	datetime2		not null,
	FeedBack_Product_ID				int					REFERENCES Products(Product_ID), 
	FeedBack_Account_ID				int					REFERENCES Account(Account_ID)
);
go
-- Thêm dữ liệu vào bảng Category
INSERT INTO [Category] (Category_Name)
VALUES ('tea'), ('coffee'), ('food'), ('package');
	go
-- Thêm dữ liệu vào bảng Account
INSERT INTO Account (Name, Password, Address, Phone, Email, Role)
VALUES 
    ('John Doe', 'password123', '123 Main St', '1234567890', 'john@example.com', 1),
    ('Alice Smith', 'securepass', '456 Elm St', '9876543210', 'alice@example.com', 0),
    ('Bob Johnson', 'mypass', '789 Oak St', '5551234567', 'bob@example.com', 1);
    -- ...Thêm 7 dòng dữ liệu khác...
		go
-- Thêm dữ liệu vào bảng [Products]
INSERT INTO [Products] (Product_image, Product_Name, Product_Price, Product_Description, ProductCategory_ID)
VALUES 
    ('product1.jpg', 'Product A', 10.00, 'Description A', 1),
    ('product2.jpg', 'Product B', 15.00, 'Description B', 2),
    ('product3.jpg', 'Product C', 25.00, 'Description C', 3);
    -- ...Thêm 7 dòng dữ liệu khác...
		go
-- Thêm dữ liệu vào bảng [Order]
INSERT INTO [Order] (Order_Status, Order_Date, Total_Price, AccountOrder_ID)
VALUES 
    (0, GETDATE(), 25.00, 1),
    (1, GETDATE(), 35.00, 2),
    (0, GETDATE(), 50.00, 3);
    -- ...Thêm 7 dòng dữ liệu khác...
		go
-- Thêm dữ liệu vào bảng [Detail_Order]
INSERT INTO [Detail_Order] (Quantity, Sub_Total, FK_Order_ID, FK_Product_ID)
VALUES 
    (2, 20.00, 1, 1),
    (1, 15.00, 2, 2),
    (3, 40.00, 3, 3);
    -- ...Thêm 7 dòng dữ liệu khác...
	go
-- Thêm dữ liệu vào bảng FeedBack
INSERT INTO FeedBack (Rate, Comment, Creation_Date, FeedBack_Product_ID, FeedBack_Account_ID)
VALUES 
    (5, 'Great product!', GETDATE(), 1, 1),
    (3, 'Could be better', GETDATE(), 2, 2),
    (4, 'Nice service', GETDATE(), 3, 3);
    -- ...Thêm 7 dòng dữ liệu khác...
go
select * from Account
select * from [Order]
select * from [Category]
select * from [Products]
select * from [Detail_Order]
select * from FeedBack