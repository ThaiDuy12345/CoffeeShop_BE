use master;
go
drop database if exists DuAn2;
go 
create database	DuAn2;
go
use DuAn2;
go

Create table [Account]
(
	Account_Phone					varchar(13)							primary key,
	Account_Name					nvarchar(255)	not null,
	Account_Password				varchar(50)		not null,
	Account_Address					nvarchar(255)	null,
	Account_Email					varchar(50)		not null			unique				check (Account_Email like '%_@_%._%'),
	Account_Role					int				not null			default(2),			check (Account_Role in (0, 1, 2)),
	Account_Active					bit				not null			default(1)
);
go

Create table [Category]
(
    Category_ID						int             identity(1,1)      primary key,
    Category_Name					nvarchar(50)    not null
);
go

Create table [Product]
(
	Product_ID						int				identity(1,1)		primary key,
	Product_Name					nvarchar(50)	not null,
	Product_Description				nvarchar(max)	not null,
	Product_Is_Popular				bit				not null			default(0),
	Product_Active					bit				not null			default(1),
	Product_Creation_Date			datetime2		not null			default(GETDATE()),
	Product_Image_Url				nvarchar(max)	not null,
	Category_ID						int				not null			references [Category](Category_ID)
);
go

Create table [Support]
(	
	Support_ID						int				identity(1,1)		primary key, 
	Support_Reason					nvarchar(100)	not null,
	Support_Title					nvarchar(100)	not null, 
	Support_Content					nvarchar(max)	not null, 
	Support_Creation_date			datetime2		not null			default(GETDATE()), 
	Support_Status					bit				not null			default(0),
	Account_Phone					varchar(13)		not null			references [Account](Account_Phone)
)
go

create table [Product_Size] 
(
	Product_Size_ID					int				identity(1,1)		primary key,
	Product_Size					varchar(1)		not null								check(Product_Size in ('S', 'M', 'L')), 
	Product_Size_Price				decimal(18, 2)	not null			default(0)			check(Product_Size_Price >= 0),
	Product_ID						int 			not null			references [Product](Product_ID),
	constraint Product_Size_Unique unique (Product_Size, Product_ID)
)
go

Create table [Favorite_Product]
(
	Account_Phone					varchar(13)		not null			references [Account](Account_Phone),	
	Product_ID						int				not null			references [Product](Product_ID),
	primary key (Account_Phone, Product_ID)
)
go

Create table [Feedback]
(
	Feedback_Rate					int				not null								check(Feedback_Rate in (1, 2, 3, 4, 5)),	
	Feedback_Comment				nvarchar(255)	not null,
	Feedback_Creation_Date			datetime2		not null			default(GETDATE()),
	Feedback_Active					bit				not null			default(1),
	Product_ID						int				not null			references [Product](Product_ID),			
	Account_Phone					varchar(13)		not null			references [Account](Account_Phone),
	primary key (Account_Phone, Product_ID)
);
go

Create table [Discount]
(
	Discount_ID						int				identity(1,1)		primary key, 
	Discount_Code					varchar(255)	not null,
	Discount_Creation_Date			datetime2		not null			default(GETDATE()),
	Discount_Expired_Date			datetime2		not null			default(GETDATE()),
	Discount_Minimum_Order_Price	decimal(18, 2)	not null			default(0)			check(Discount_Minimum_Order_Price >= 0),
	Discount_Amount					decimal(18, 2)	not null			default(1000)		check(Discount_Amount > 0)
)
go

Create table [Ordering]
(
	Ordering_ID						int				identity(1,1)		primary key,
	Ordering_Status					int				not null			default(0)			check(Ordering_Status in (0, 1, 2, 3, 4, -1)),
	Ordering_Creation_Date			datetime2		not null			default(GETDATE()),
	Ordering_Shipping_Fee			decimal(18, 2)	not null			default(15000)		check(Ordering_Shipping_Fee > 0),
	Ordering_Price					decimal(18, 2)	not null			default(0)			check(Ordering_Price >= 0),
	Ordering_Total_Price			decimal(18, 2)	not null			default(0)			check(Ordering_Total_Price >= 0),
	Ordering_Note					nvarchar(255)	null, 
	Account_Phone					varchar(13)		not null			references [Account](Account_Phone),
	Discount_ID						int				null				references [Discount](Discount_ID)
);
go

Create table [Detail_Order]
(
	Detail_Order_Product_Quantity	int				not null			default(1)			check(Detail_Order_Product_Quantity > 0),
	Detail_Order_Sub_Total			decimal(18, 2)	not null			default(0)			check(Detail_Order_Sub_Total >= 0),
	Ordering_ID						int				not null			references [Ordering](Ordering_ID), 
	Product_Size_ID					int				not null			references [Product_Size](Product_Size_ID),
	primary key (Ordering_ID, Product_Size_ID)
);
go

create trigger Update_Price_Trigger
on [Detail_Order]
for insert, update, delete
as
begin
	declare @ordering_id int
	declare @ordering_price decimal(18, 2)
	
	if exists (select * from deleted)
	begin
		-- Handle DELETE operation
		declare cur_cursor cursor for
		select ordering_id from deleted

		open cur_cursor
		fetch next from cur_cursor into @ordering_id

		while @@fetch_status = 0
		begin
			-- Update Detail_Order_Sub_Total based on remaining Detail_Order records
			update do
			set Detail_Order_Sub_Total = do.Detail_Order_Product_Quantity * ps.Product_Size_Price
			from [Detail_Order] do
			inner join [Product_Size] ps on do.Product_Size_ID = ps.Product_Size_ID
			where do.Ordering_ID = @ordering_id

			-- Recalculate total price for the ordering
			set @ordering_price = (
				select sum(do.Detail_Order_Sub_Total) as total_price
				from [Detail_Order] do
				where do.Ordering_ID = @ordering_id
			)

			-- Update Ordering based on total price
			if (Select Discount_id from [Ordering] where Ordering_ID = @ordering_id) is null 
			begin
				update [Ordering]
				set Ordering_Price = @ordering_price, Ordering_Total_Price = @ordering_price + Ordering_Shipping_Fee
				where Ordering_ID = @ordering_id
			end
			else
			begin
				update [Ordering]
				set Ordering_Price = @ordering_price, Ordering_Total_Price = (@ordering_price + Ordering_Shipping_Fee) - d.Discount_Amount
				from Discount d
				where Ordering_ID = @ordering_id and [Ordering].Discount_ID = d.Discount_ID
			end

			fetch next from cur_cursor into @ordering_id
      end
    end
	else
	begin
        -- Duyệt qua từng bản ghi trong inserted
        declare cur_cursor cursor for
        select ordering_id from inserted

        open cur_cursor
        fetch next from cur_cursor into @ordering_id

        while @@fetch_status = 0
        begin
            update [Detail_Order]
            set Detail_Order_Sub_Total = Detail_Order_Product_Quantity * Product_Size.Product_Size_Price
            from [Product_Size]
            where Detail_Order.Product_Size_ID = Product_Size.Product_Size_ID

            set @ordering_price = (select sum(do.Detail_Order_Sub_Total) as total_price from [Detail_Order] do
                                inner join [Ordering] o on o.Ordering_ID = do.Ordering_ID
                                where o.Ordering_ID = @ordering_id)

            if (Select Discount_id from [Ordering] where Ordering_ID = @ordering_id) is null 
            begin
                update [Ordering]
                set Ordering_Price = @ordering_price, Ordering_Total_Price =  @ordering_price + Ordering_Shipping_Fee
                where Ordering_ID = @ordering_id
            end
            else
            begin
                update [Ordering]
                set Ordering_Price = @ordering_price, Ordering_Total_Price = (@ordering_price + Ordering_Shipping_Fee) - d.Discount_Amount
                from Discount d
                where Ordering_ID = @ordering_id and [Ordering].Discount_ID = d.Discount_ID
            end

            fetch next from cur_cursor into @ordering_id
        end
    end

	close cur_cursor
	deallocate cur_cursor
end
go

-- Thêm dữ liệu vào bảng [Category]
INSERT INTO [Category] (Category_Name)
VALUES (N'Trà'), (N'Cà phê'), (N'Đồ ăn');
go

-- Thêm dữ liệu vào bảng [Account]
INSERT INTO [Account] (Account_Phone, Account_Name, Account_Password, Account_Address, Account_Email)
VALUES 
  ('+841234567891', N'Admin_Coffee', '123123aA', N'Hà Nội', 'Admin@gmail.com')
go

-- Thêm dữ liệu vào bảng [Product]
INSERT INTO [Product] (Product_Name, Product_Description, Product_Image_Url, Category_ID)
VALUES 
  (N'Trà Xanh', N'Trà xanh nguyên chất', '/main/app/image1.url', 1),
  (N'Cà Phê Robusta', N'Cà phê Robusta chất lượng cao', '/main/app/image2.url', 2),
  (N'Bánh Mì Sandwich', N'Bánh mì sandwich thơm ngon', '/main/app/image3.url', 3),
  (N'Trà Đào', N'Trà đào mát lạnh', '/main/app/image4.url', 1),
  (N'Cà Phê Arabica', N'Cà phê Arabica đắng ngắt', '/main/app/image5.url', 2);
go

-- Thêm dữ liệu vào bảng [Product_Size]
INSERT INTO [Product_Size] (Product_Size, Product_Size_Price, Product_ID)
VALUES 
  ('S', 18000, 1),
  ('L', 20000, 1),
  ('S', 20000, 2),
  ('M', 22000, 2),
  ('L', 24000, 2),
  ('S', 20000, 3),
  ('M', 22000, 3),
  ('L', 24000, 3),
  ('S', 20000, 4),
  ('M', 22000, 4),
  ('L', 24000, 4)
go

-- Thêm dữ liệu vào bảng [Favorite_Product]
INSERT INTO [Favorite_Product] (Account_Phone, Product_ID)
VALUES 
  ('+841234567891', 3),
  ('+841234567891', 1),
  ('+841234567891', 5),
  ('+841234567891', 2),
  ('+841234567891', 4);
go

-- Thêm dữ liệu vào bảng [Discount]
INSERT INTO [Discount] (Discount_Code, Discount_Minimum_Order_Price, Discount_Amount)
VALUES 
  ('DISC10', 50000, 10000),
  ('FREESHIP', 500000, 100000),
  ('SALE15', 1000000, 200000),
  ('DISC20', 60000, 10000),
  ('NEWUSER', 70000, 10000);
go

-- Thêm dữ liệu vào bảng [Order]
INSERT INTO [Ordering] (Ordering_Note, Account_Phone, Discount_ID)
VALUES 
  (N'Giao hàng nhanh', '+841234567891', 5),
  (N'Giao hàng tiêu chuẩn', '+841234567891', 5),
  (N'Khách hàng đặc biệt', '+841234567891', null),
  (N'Giao hàng gấp', '+841234567891', null),
  (N'Khách hàng VIP', '+841234567891', null);
go

-- Thêm dữ liệu vào bảng [Detail_Order]
INSERT INTO [Detail_Order] (Detail_Order_Product_Quantity, Ordering_ID, Product_Size_ID)
VALUES 
  (1, 3, 1),
  (1, 2, 2),
  (3, 2, 3),
  (1, 2, 4),
  (2, 2, 5)
go

-- Thêm dữ liệu vào bảng [FeedBack]
INSERT INTO [FeedBack] (Product_ID, Account_Phone, Feedback_Comment, Feedback_Rate)
VALUES 
  (1, '+841234567891', N'Rất hài lòng với sản phẩm này.', 4),
  (2, '+841234567891', N'Chất lượng ổn, giá hơi cao.', 4),
  (3, '+841234567891', N'Sản phẩm tuyệt vời, giao hàng nhanh.', 5),
  (4, '+841234567891', N'Sản phẩm không như mong đợi.', 4),
  (5, '+841234567891', N'Hỗ trợ khách hàng rất tốt.', 5);
go

-- Thêm dữ liệu vào bảng [Support]
INSERT INTO [Support] (Support_Reason, Account_Phone, Support_Title, Support_Content)
VALUES 
  (N'Đổi trả hàng', '+841234567891', N'Hỗ trợ đổi trả', N'Tôi muốn đổi sản phẩm do không vừa size.'),
  (N'Hỏng hóc', '+841234567891', N'Cần hỗ trợ sửa chữa', N'Sản phẩm của tôi bị hỏng, làm thế nào để sửa?')


-- Hiển thị dữ liệu
SELECT * FROM [Account];
go
SELECT * FROM [Ordering];
go
SELECT * FROM [Category];
go
SELECT * FROM [Product];
go
SELECT * FROM [Favorite_Product];
go
SELECT * FROM [Detail_Order];
go
SELECT * FROM [FeedBack];
go
SELECT * FROM [Discount];
go
SELECT * FROM [Support];
go
