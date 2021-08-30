library(testthat)

home_dir <- '/app/tests'

setwd(home_dir)

reporter <- MultiReporter$new(list(
  StopReporter$new(),
  JunitReporter$new(file = "tests.xml")
))

testthat::test_file("test.R", reporter = reporter)
