library(testthat)
library(httr)
library(stringr)
library(jsonlite)

BASE_URL <- 'localhost'

PATH_EVAL <- '/eval'
PATH_PING <- '/ping'

MIME_TYPE_JSON <- 'application/json'
MIME_TYPE_PNG <- 'image/png'
MIME_TYPE_PDF <- 'application/pdf'

BASE_EVAL_URL <- paste0(BASE_URL, PATH_EVAL)

test_that('checkPulse', {
  url <- paste0(BASE_URL, PATH_PING)
  response <- GET(url=url)
  response_content <- fromJSON(content(response, 'text'))
  expect_equal(response$status_code, 200)
  expect_equal(response$headers$`content-type`, MIME_TYPE_JSON)
  # expect_true(str_detect(response_content, 'ms'))
})

test_that('checkEval', {
  url <- BASE_EVAL_URL
  request_body <- 'output <- 1 + 1'
  response <- POST(url=url, body=request_body)
  response_content <- fromJSON(content(response, 'text'))
  expect_equal(response$status_code, 200)
  expect_equal(response$headers$`content-type`, MIME_TYPE_JSON)
  expect_equal(response_content, 2)
})

test_that('checkMainPackages', {
  url <- BASE_EVAL_URL
  request_body <- 'output <- as.data.frame(installed.packages()[,c(1,3:4)]); rownames(output) <- NULL; output <- output[is.na(output$Priority),1:2,drop=FALSE]'
  response <- POST(url=url, body=request_body)
  packages <- fromJSON(content(response, 'text'))
  expect_equal(response$status_code, 200)
  expect_equal(response$headers$`content-type`, MIME_TYPE_JSON)
  expect_true(any(packages == 'jsonlite'))
  expect_true(any(packages == 'usethis'))
  expect_true(any(packages == 'devtools'))
  expect_true(any(packages == 'dplyr'))
  expect_true(any(packages == 'lubridate'))
  expect_true(any(packages == 'stringr'))
  expect_true(any(packages == 'httr'))
  expect_true(any(packages == 'knitr'))
  expect_true(any(packages == 'curl'))
  expect_true(any(packages == 'rmarkdown'))
  expect_true(any(packages == 'qpdf'))
  expect_true(any(packages == 'pdftools'))
  expect_true(any(packages == 'ggplot2'))
  expect_true(any(packages == 'telegram.bot'))
})

test_that('checkImage', {
  url <- paste0(BASE_EVAL_URL,'?type=image')
  request_body <- 'plot(iris)'
  response <- POST(url=url, body=request_body)
  expect_equal(response$status_code, 200)
  expect_equal(response$headers$`content-type`, MIME_TYPE_PNG)
})

test_that('checkCreateDocument', {
  url <- paste0(BASE_EVAL_URL,'?type=document')
  request_body <- '\\documentclass{article}\n\n\\begin{document}\nHello World\n\\end{document}'
  response <- POST(url=url, body=request_body)
  expect_equal(response$status_code, 200)
  expect_equal(response$headers$`content-type`, MIME_TYPE_PDF)
  expect_gte(as.numeric(response$headers$`content-length`), 11280)
})

test_that('checkCreateDocumentByFile', {
  filename_pdf <- 'helloworld.pdf'
  url <- paste0(BASE_EVAL_URL,'/file?filename=hello.Rnw&type=document')
  request_body <-''
  response <- POST(url=url, body=request_body)
  bin <- content(response, 'raw')
  tmp <- writeBin(bin, filename_pdf)
  file_size <- file.info(filename_pdf)$size
  expect_equal(response$status_code, 200)
  expect_equal(response$headers$`content-type`, MIME_TYPE_PDF)
  expect_gte(file_size, 11280)
})