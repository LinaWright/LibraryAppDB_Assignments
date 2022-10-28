package com.library.steps;

import com.library.pages.BookPage;
import com.library.pages.BorrowedBooksPage;
import com.library.pages.DashBoardPage;
import com.library.utility.BrowserUtil;
import com.library.utility.DB_Util;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.junit.Assert;
import org.openqa.selenium.WebElement;

import java.util.List;
import java.util.Map;

public class BookStepDefs {
    BookPage bookPage = new BookPage();

    @When("the user navigates to {string} page")
    public void the_user_navigates_to_page(String module) {
        bookPage.navigateModule(module);
    }

    List<String> actualList;

    @When("the user clicks book categories")
    public void the_user_clicks_book_categories() {
        actualList = BrowserUtil.getAllSelectOptions(bookPage.mainCategoryElement);
        actualList.remove(0);//remove the category name
        System.out.println("actualList = " + actualList);
    }

    @Then("verify book categories must match book_categories table from db")
    public void verify_book_categories_must_match_book_categories_table_from_db() {
        String query = "select name from book_categories";
        DB_Util.runQuery(query);
        List<String> expectedCategoryList = DB_Util.getColumnDataAsList("name");
        Assert.assertEquals(expectedCategoryList, actualList);
    }

    //06

    @When("the librarian click to add book")
    public void the_librarian_click_to_add_book() {
        bookPage.addBook.click();
    }

    @When("the librarian enter book name {string}")
    public void the_librarian_enter_book_name(String name) {
        bookPage.bookName.sendKeys(name);
    }

    @When("the librarian enter ISBN {string}")
    public void the_librarian_enter_isbn(String isbn) {
        bookPage.isbn.sendKeys(isbn);
    }

    @When("the librarian enter year {string}")
    public void the_librarian_enter_year(String year) {
        bookPage.year.sendKeys(year);
    }

    @When("the librarian enter author {string}")
    public void the_librarian_enter_author(String author) {
        bookPage.year.sendKeys(author);
    }

    @When("the librarian choose the book category {string}")
    public void the_librarian_choose_the_book_category(String category) {
        BrowserUtil.selectOptionDropdown(bookPage.categoryDropdown, category);
    }

    @When("the librarian click to save changes")
    public void the_librarian_click_to_save_changes() {
        bookPage.saveChanges.click();
    }

    @Then("verify “The book has been created\" message is displayed")
    public void verify_the_book_has_been_created_message_is_displayed() throws InterruptedException {
        wait(3);
        Assert.assertTrue(bookPage.toastMessage.isDisplayed());
    }

    @Then("verify {string} information must match with DB")
    public void verify_information_must_match_with_db(String expectedBookName) {
        String query = "select name,author,isbn from books\n" +
                "where name = '"+expectedBookName+"'";
        DB_Util.runQuery(query);
        Map<String, String> rowMap = DB_Util.getRowMap(1);
        String actualBookName = rowMap.get("name");
        Assert.assertEquals(expectedBookName,actualBookName);
    }

    //07
    String bookName;
    @Given("the user searches for {string} book")
    public void the_user_searches_for_book(String name) {
        bookName = name;
        bookPage.search.sendKeys(name);
    }
    @When("the user clicks Borrow Book")
    public void the_user_clicks_borrow_book() {
        bookPage.borrowBook(bookName);
    }
    @Then("verify that book is shown in \"Borrowing Books” page")
    public void verify_that_book_is_shown_in_borrowing_books_page() {
        BorrowedBooksPage borrowedBooksPage = new BorrowedBooksPage();
        new DashBoardPage().navigateModule("Borrowing Books");
        List<WebElement> allBorrowedBooksName = borrowedBooksPage.allBorrowedBooksName;
        Assert.assertTrue(BrowserUtil.getElementsText(borrowedBooksPage.allBorrowedBooksName).contains(bookName));
    }
    @Then("verify logged student has same book in database")
    public void verify_logged_student_has_same_book_in_database() {
        // Write code here that turns the phrase above into concrete actions
        throw new io.cucumber.java.PendingException();
    }


}
