package com.example.jobscrapper.service;

import com.example.jobscrapper.model.JobAd;
import com.example.jobscrapper.repository.JobAdRepository;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.interactions.WheelInput;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.Duration;
import java.time.LocalDate;
import java.time.Month;
import java.util.ArrayList;
import java.util.List;

import static java.util.Objects.isNull;

@Service
public class ScrappingService {
    public static final String JOBS_PAGE_URL = "https://jobs.techstars.com/jobs";
    public static final String GET_JOB_FUNCTIONS_BUTTON_CLASSES = "sc-dmqHEX enTheS";
    public static final String JOBS_FUNCTIONS_BUTTONS_CLASSES = "sc-beqWaB cTPvSS";
    public static final String CLOSE_COOKIES_ALERT_CLASSES = "onetrust-close-btn-handler onetrust-close-btn-ui banner-close-button ot-close-icon";
    public static final String FUNCTION_LIST_CLASSES = "sc-beqWaB sc-gueYoa fyuEMV MYFxR";
    public static final String JOB_ELEMENT_CLASSES = "sc-beqWaB sc-gueYoa diHipZ MYFxR";
    public static final String LOGO_LINK_CLASSES = "sc-dmqHEX eTCoCQ";
    public static final String LOAD_MORE_BUTTON_CLASSES = "sc-dmqHEX mPwyE";
    public static final String FOOTER_CLASSES = "sc-beqWaB sc-gueYoa iSijfj MYFxR";
    public static final String TITLE_CLASSES = "sc-beqWaB kToBwF";
    public static final String ORGANISATION_NAME_CLASSES = "sc-beqWaB iwxlWP theme_only";
    public static final String LOCATION_ATTRIBUTE_VALUE = "address";
    public static final String JOB_PAGE_URL_CLASSES = "sc-beqWaB fVVkSZ theme_only";
    public static final String ORGANISATION_URL_BUTTON_CLASSES = "sc-dmqHEX WPfrO";
    public static final String DESCRIPTION_ATTR = "data-testid";
    public static final String DESCRIPTION_ATTR_VALUE = "careerPage";
    public static final String TAG_CLASSES = "sc-dmqHEX OHsAR";

    @Autowired
    JobAdRepository jobAdRepository;

    @Value("${LOAD_FULL_PAGE}")
    Boolean isLoadFullPage;

    public WebDriver getJobsPageByFunction(String function) {
        WebDriver driver = new ChromeDriver();
        driver.get(JOBS_PAGE_URL);
        driver.manage().timeouts().implicitlyWait(Duration.ofMillis(10000));

        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        WebElement closeCookiesButton = driver.findElement(By.cssSelector("button[class='" + CLOSE_COOKIES_ALERT_CLASSES + "']"));
        closeCookiesButton.click();

        WebElement jobFunctionButton = driver.findElement(By.cssSelector("button[class='" + GET_JOB_FUNCTIONS_BUTTON_CLASSES + "']"));
        jobFunctionButton.click();

        boolean isFunctionFound = false;
        List<WebElement> jobsCategories = driver.findElements(By.cssSelector("button[class='" + JOBS_FUNCTIONS_BUTTONS_CLASSES + "']"));
        for (WebElement categories : jobsCategories) {
            if (categories.getText().replace("& ", "").equals(function)) {
                categories.click();
                isFunctionFound = true;
                break;
            }
        }

        if (!isFunctionFound) {
            WebElement jobFunctionList = driver.findElement(By.cssSelector("div[class='" + FUNCTION_LIST_CLASSES + "']"));
            WheelInput.ScrollOrigin scrollOrigin = WheelInput.ScrollOrigin.fromElement(jobFunctionList);
            new Actions(driver)
                    .scrollFromOrigin(scrollOrigin, 0, 200)
                    .perform();

            jobsCategories = driver.findElements(By.cssSelector("button[class='" + JOBS_FUNCTIONS_BUTTONS_CLASSES + "']"));
            for (WebElement categories : jobsCategories) {
                if (categories.getText().replace("& ", "").equals(function)) {
                categories.click();
                isFunctionFound = true;
                break;
                }
            }
        }

        if (isFunctionFound) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }

            return driver;
        } else {
            throw new IllegalArgumentException("This job function doesn't exist");
        }

    }

    public List<WebElement> loadAllData(WebDriver driver) {
        List<WebElement> jobElements = driver.findElements(By.cssSelector("div[class='" + JOB_ELEMENT_CLASSES + "']"));
        int prevJobsCount = 0;

        while (prevJobsCount != jobElements.size()) {
            try {
                Thread.sleep(3500);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }

            WebElement footerElement = driver.findElement(By.cssSelector("div[class='" + FOOTER_CLASSES + "']"));
            new Actions(driver)
                    .scrollToElement(footerElement)
                    .perform();

            prevJobsCount = jobElements.size();
            jobElements = driver.findElements(By.cssSelector("div[class='" + JOB_ELEMENT_CLASSES + "']"));
        }

        return jobElements;
    }

    public String getLogoLink(WebElement element) {
        String logoLink;
        try {
            logoLink = element.findElement(By.cssSelector("img[class='" + LOGO_LINK_CLASSES + "']"))
                    .getAttribute("src");
        } catch (Exception e) {
            logoLink = "NOT_FOUND";
        }
        return logoLink;
    }

    public String getPositionName(WebElement element) {
        String positionName;
        try {
            positionName = element.findElement(By.cssSelector("div[class='" + TITLE_CLASSES + "']")).getText();
        } catch (Exception e) {
            positionName = "NOT_FOUND";
        }
        return positionName;
    }

    public String getOrganisationTitle(WebElement element) {
        String organisationName;
        try {
            organisationName = element.findElement(By.cssSelector("a[class='" + ORGANISATION_NAME_CLASSES + "']")).getText();
        } catch (Exception e) {
            organisationName = "NOT_FOUND";
        }
        return organisationName;
    }

    public String getLocation(WebElement element) {
        String location;
        try {
            location = element.findElement(By.cssSelector("meta[itemprop='" + LOCATION_ATTRIBUTE_VALUE + "']"))
                    .getAttribute("content");
        } catch (Exception e) {
            location = "NOT_FOUND";
        }
        return location;
    }

    public String getJobsPageUrl(WebElement element) {
        String jobPageUrl;
        try {
            jobPageUrl = element.findElement(By.cssSelector("a[class='" + JOB_PAGE_URL_CLASSES + "']"))
                    .getAttribute("href");
        } catch (Exception e) {
            jobPageUrl = "NOT_FOUND";
        }
        return jobPageUrl;
    }

    public String getOrganisationUrl(Document document) {
        String organisationUrl;
        try {
            organisationUrl = document.getElementsByClass(ORGANISATION_URL_BUTTON_CLASSES)
                    .attr("href");
            if (organisationUrl.isBlank()) {
                organisationUrl = "NOT_FOUND";
            }
        } catch (Exception e) {
            organisationUrl = "NOT_FOUND";
        }
        return organisationUrl;
    }

    public String getDescription(Document document) {
        String description;
        try {
            description = document.getElementsByAttributeValue(DESCRIPTION_ATTR, DESCRIPTION_ATTR_VALUE)
                    .html();
            if (description.isBlank()) {
                description = "NOT_FOUND";
            }
        } catch (Exception e) {
            description = "NOT_FOUND";
        }
        return description;
    }

    public String getTags(WebElement element) {
        String tags = "";
        try {
            List<WebElement> tagsElements = element.findElements(By.cssSelector("div[class='" + TAG_CLASSES + "']"));
            for (WebElement tag : tagsElements) {
                tags += tag.getText() + ", ";
            }
            tags = tags.substring(0, tags.length()-2);
        } catch (Exception e) {
            tags = "NOT_FOUND";
        }
        return tags;
    }

    public LocalDate getPostedDate(Document document) {
        String postedDate;
        try {
            postedDate = document.getElementsByClass("sc-beqWaB gRXpLa")
                    .text();
            if (postedDate.isBlank()) {
                postedDate = null;
            }
        } catch (Exception e) {
            postedDate = null;
        }
        if (isNull(postedDate)) {
            return null;
        }

        return parseLocalDate(postedDate);
    }

    public LocalDate parseLocalDate(String postedDate) {
        LocalDate date;
        try {
            int day = Integer.parseInt(postedDate.split(", ")[1].split(" ")[1]);
            int year = Integer.parseInt(postedDate.split(", ")[2]);
            Month month = Month.valueOf(postedDate.split(", ")[1].split(" ")[0].toUpperCase());

            date = LocalDate.of(year, month, day);
        } catch (Exception e) {
            date = null;
        }
        return date;
    }

    public void parseJobPage(JobAd jobAd) {
        Document document;
        try {
            document = Jsoup.connect(jobAd.getJobPageUrl()).get();
        } catch (Exception e) {
            jobAd.setOrganisationUrl("NOT_FOUND");
            jobAd.setDescription("NOT_FOUND");
            jobAd.setPostedDate(null);
            return;
        }
        jobAd.setOrganisationUrl(getOrganisationUrl(document));
        jobAd.setDescription(getDescription(document));
        jobAd.setPostedDate(getPostedDate(document));
    }

    public List<JobAd> collectDataFromJobList(WebDriver driver, String function) {
        List<JobAd> jobs = new ArrayList<>();
        List<WebElement> jobElements;
        if (isLoadFullPage) {
            WebElement loadMoreButton = driver.findElement(By.cssSelector("button[class='" + LOAD_MORE_BUTTON_CLASSES + "']"));
            loadMoreButton.click();
            jobElements = loadAllData(driver);
        } else {
            jobElements = driver.findElements(By.cssSelector("div[class='" + JOB_ELEMENT_CLASSES + "']"));
        }

        for (WebElement jobElement : jobElements) {
            JobAd job = new JobAd();
            job.setLogo(getLogoLink(jobElement));
            job.setPositionName(getPositionName(jobElement));
            job.setOrganisationTitle(getOrganisationTitle(jobElement));
            job.setLocation(getLocation(jobElement));
            job.setLaborFunction(function);
            job.setJobPageUrl(getJobsPageUrl(jobElement));
            job.setTags(getTags(jobElement));
            parseJobPage(job);
            jobs.add(job);
        }

        driver.quit();

        return jobs;
    }

    public List<JobAd> saveAndReturnJobList(List<JobAd> jobs) {
        return jobAdRepository.saveAll(jobs);
    }

    public List<JobAd> getJobsResult(String function) {
        return saveAndReturnJobList(collectDataFromJobList(getJobsPageByFunction(function), function));
    }

    public List<JobAd> getJobs() {
        return jobAdRepository.findAll();
    }

    public void deleteJobs() {
        jobAdRepository.deleteAll();
    }
}
