# -*- coding: utf-8 -*-
from selenium import webdriver
from selenium.webdriver.common.by import By
from selenium.webdriver.common.keys import Keys
from selenium.webdriver.support.ui import Select
from selenium.common.exceptions import NoSuchElementException
from selenium.common.exceptions import NoAlertPresentException
import unittest, time, re

class DeployFormTest(unittest.TestCase):
    def setUp(self):
        self.driver = webdriver.Firefox()
        self.driver.implicitly_wait(30)
        self.base_url = "http://kf.kbtdev.org/"
        self.verificationErrors = []
        self.accept_next_alert = True
    
    def test_deploy_form(self):
        driver = self.driver
        driver.get(self.base_url + "")
        for i in range(60):
            try:
                if self.is_element_present(By.CSS_SELECTOR, ".forms__kobocat__btn--publish"): break
            except: pass
            time.sleep(1)
        else: self.fail("time out")
        driver.find_element_by_css_selector(".forms__kobocat__btn--publish").click()
        for i in range(60):
            try:
                if self.is_element_present(By.CSS_SELECTOR, ".forms__kobocat__publisher"): break
            except: pass
            time.sleep(1)
        else: self.fail("time out")
        self.assertTrue(self.is_element_present(By.CSS_SELECTOR, ".forms__kobocat__publisher + .ui-dialog-buttonpane .save-button"))
        driver.find_element_by_css_selector(".forms__kobocat__publisher + .ui-dialog-buttonpane .save-button").click()
        for i in range(60):
            try:
                if self.is_element_present(By.CSS_SELECTOR, ".single-project__header #title"): break
            except: pass
            time.sleep(1)
        else: self.fail("time out")
        self.assertEqual("Selenium test form title.", driver.find_element_by_css_selector(".single-project__header #title").get_attribute("value"))
    
    def is_element_present(self, how, what):
        try: self.driver.find_element(by=how, value=what)
        except NoSuchElementException, e: return False
        return True
    
    def is_alert_present(self):
        try: self.driver.switch_to_alert()
        except NoAlertPresentException, e: return False
        return True
    
    def close_alert_and_get_its_text(self):
        try:
            alert = self.driver.switch_to_alert()
            alert_text = alert.text
            if self.accept_next_alert:
                alert.accept()
            else:
                alert.dismiss()
            return alert_text
        finally: self.accept_next_alert = True
    
    def tearDown(self):
        self.driver.quit()
        self.assertEqual([], self.verificationErrors)

if __name__ == "__main__":
    unittest.main()
