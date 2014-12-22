library(ggplot2)
library(reshape2)

times.plain <- c(2, 1, 2, 3, 5, 17, 19, 31, 45, 99, 245, 455, 797, 1726, 3428, 7292, 14826, 63114, 90805, 158206)
times.sb <- c(1, 0, 1, 2, 4, 7, 12, 15, 26, 51, 95, 202, 380, 811, 1526, 3185, 6412, 13435, 26367, 52828)

dat <- data.frame(size=seq(20), plain=times.plain, sb=times.sb)

dat2 <- melt(dat, id.vars = 'size')

ggplot(dat2, aes(x=size, y=value, color=variable)) + geom_point()

within(dat, ratio <- plain/sb)
