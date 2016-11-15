package com.github.haretaro.twitterscraper

import org.openqa.selenium.htmlunit.HtmlUnitDriver
import org.openqa.selenium.WebElement
import org.openqa.selenium.NoSuchElementException
import scala.collection.JavaConversions._
import java.io.File
import scala.sys.process._
import org.openqa.selenium.By


object Main extends App{
  def url = "https://twitter.com/search?f=tweets&vertical=default&q=since%3A2015-01-01%20until%3A2015-01-02%20%E6%AD%BB%E3%81%AC&src=typd"

  /**
   * ツイートを返す
   */
  def content(tweet: WebElement): String = {
    val content = tweet.findElement(By.xpath(".//div[@class='tweet-text']"))
    content.getText()
  }

  def crawl(driver: HtmlUnitDriver, f: HtmlUnitDriver => List[String]): List[String] = {
    def func(driver: Option[HtmlUnitDriver]): List[List[String]] = driver match{
      case Some(driver) => f(driver) :: func(nextPage(driver))
      case _ => Nil
    }
    func(Some(driver)).flatten
  }

  def nextPage(driver: HtmlUnitDriver) = {
    try{
      val link = driver.findElementByXPath("//div[@class='w-button-more']/a")
      link.click()
      Some(driver)
    }catch{
      case e:NoSuchElementException => None
    }
  }

  def search(query: String) = {
    val driver = new HtmlUnitDriver()
    driver.get("https://mobile.twitter.com/search")
    val form = driver.findElementByXPath("//input[@id='q']")
    form.sendKeys(query)
    form.submit()
    driver
  }

  def tweets(driver: HtmlUnitDriver) = {
    //val output = "output.html"
    //"echo %s".format(driver.getPageSource()) #>> new File(output)!

    driver.findElementsByXPath("//table[@class='tweet  ']")
  }

  /**
   * ユーザー名を返す
   */
  def username(tweet: WebElement): String = {
    val name = tweet.findElement(By.xpath(".//div[@class='username']"))
    name.getText()
  }

}
