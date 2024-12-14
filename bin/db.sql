CREATE DATABASE CaLouselF;

USE CaLouselF;

CREATE TABLE Users (
    UserID INT AUTO_INCREMENT PRIMARY KEY,
    Username VARCHAR(255) NOT NULL UNIQUE,
    PasswordHash VARCHAR(255) NOT NULL,
    PhoneNumber VARCHAR(15) NOT NULL,
    Address TEXT NOT NULL,
    Role ENUM('Buyer', 'Seller') NOT NULL
);

CREATE TABLE Items (
    ItemID INT AUTO_INCREMENT PRIMARY KEY,
    SellerID INT NOT NULL,
    ItemName VARCHAR(255) NOT NULL,
    Category VARCHAR(255) NOT NULL,
    Size VARCHAR(50) NOT NULL,
    Price DECIMAL(10, 2) NOT NULL,
    Status ENUM('Pending', 'Approved', 'Declined') DEFAULT 'Pending',
    DeclineReason TEXT,
    FOREIGN KEY (SellerID) REFERENCES Users(UserID)
);

CREATE TABLE Wishlist (
    WishlistID INT AUTO_INCREMENT PRIMARY KEY,
    BuyerID INT NOT NULL,
    ItemID INT NOT NULL,
    FOREIGN KEY (BuyerID) REFERENCES Users(UserID),
    FOREIGN KEY (ItemID) REFERENCES Items(ItemID)
);

CREATE TABLE Transactions (
    TransactionID INT AUTO_INCREMENT PRIMARY KEY,
    BuyerID INT NOT NULL,
    ItemID INT NOT NULL,
    TotalPrice DECIMAL(10, 2) NOT NULL,
    FOREIGN KEY (BuyerID) REFERENCES Users(UserID),
    FOREIGN KEY (ItemID) REFERENCES Items(ItemID)
);

CREATE TABLE Offers (
    OfferID INT AUTO_INCREMENT PRIMARY KEY,
    ItemID INT NOT NULL,
    BuyerID INT NOT NULL,
    OfferPrice DECIMAL(10, 2) NOT NULL,
    Status ENUM('Pending', 'Accepted', 'Declined') DEFAULT 'Pending',
    DeclineReason TEXT,
    FOREIGN KEY (ItemID) REFERENCES Items(ItemID),
    FOREIGN KEY (BuyerID) REFERENCES Users(UserID)
);
