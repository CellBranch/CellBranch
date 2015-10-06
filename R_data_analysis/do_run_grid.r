pdf("single_cistrome_grid.pdf", bg="white");

data <- read.csv("n4_o3/200/7/69/simOutputData.csv", sep="");

time1 <- data[[1]];
nanog_m7_reds <- data[[2]];
nanog_m7_shared_reds <- data[[6]];

nanog_m7_prop_active <- 100 * (nanog_m7_reds + nanog_m7_shared_reds) / 631;

data <- read.csv("n4_o6/200/8/69/simOutputData.csv", sep="");

time2 <- data[[1]];
nanog_m8_reds <- data[[2]];
nanog_m8_shared_reds <- data[[6]];

nanog_m8_prop_active <- 100 * (nanog_m8_reds + nanog_m8_shared_reds) / 631;

data <- read.csv("n4_o12/200/9/69/simOutputData.csv", sep="");

time3 <- data[[1]];
nanog_m9_reds <- data[[2]];
nanog_m9_shared_reds <- data[[6]];

nanog_m9_prop_active <- 100 * (nanog_m9_reds + nanog_m9_shared_reds) / 631;

data <- read.csv("n8_o3/200/5/69/simOutputData.csv", sep="");

time4 <- data[[1]];
oct4_m5_reds <- data[[2]];
oct4_m5_shared_reds <- data[[6]];

oct4_m5_prop_active <- 100 * (oct4_m5_reds + oct4_m5_shared_reds) / 466;

data <- read.csv("n8_o6/200/6/69/simOutputData.csv", sep="");

time5 <- data[[1]];
oct4_m6_reds <- data[[2]];
oct4_m6_shared_reds <- data[[6]];

oct4_m6_prop_active <- 100 * (oct4_m6_reds + oct4_m6_shared_reds) / 466;

data <- read.csv("n8_o12/200/7/69/simOutputData.csv", sep="");

time6 <- data[[1]];
oct4_m7_reds <- data[[2]];
oct4_m7_shared_reds <- data[[6]];

oct4_m7_prop_active <- 100 * (oct4_m7_reds + oct4_m7_shared_reds) / 466;

data <- read.csv("n16_o3/200/6/69/simOutputData.csv", sep="");

time7 <- data[[1]];
sox2_m6_reds <- data[[2]];
sox2_m6_shared_reds <- data[[6]];

sox2_m6_prop_active <- 100 * (sox2_m6_reds + sox2_m6_shared_reds) / 466;

data <- read.csv("n16_o6/200/7/69/simOutputData.csv", sep="");

time8 <- data[[1]];
sox2_m7_reds <- data[[2]];
sox2_m7_shared_reds <- data[[6]];

sox2_m7_prop_active <- 100 * (sox2_m7_reds + sox2_m7_shared_reds) / 466;

data <- read.csv("n16_o12/200/8/69/simOutputData.csv", sep="");

time9 <- data[[1]];
sox2_m8_reds <- data[[2]];
sox2_m8_shared_reds <- data[[6]];

sox2_m8_prop_active <- 100 * (sox2_m8_reds + sox2_m8_shared_reds) / 466;

par(mfrow = c(3,3));

plot(time1, nanog_m7_prop_active, type="l", main="nanog m=mcrit-1", xlab= "time", ylab = "Percentage of activated red posts", col="red", xlim=range(c(0,1000)), ylim=range(c(0,100)));
plot(time2, nanog_m8_prop_active, type="l", main="nanog m=mcrit", xlab= "time", ylab = "Percentage of activated red posts", col="red", xlim=range(c(0,1000)), ylim=range(c(0,100)));
plot(time3, nanog_m9_prop_active, type="l", main="nanog m=mcrit+1", xlab= "time", ylab = "Percentage of activated red posts", col="red", xlim=range(c(0,1000)), ylim=range(c(0,100)));

plot(time4, oct4_m5_prop_active, type="l", main="oct4 m=mcrit-1", xlab= "time", ylab = "Percentage of activated red posts", col="blue", xlim=range(c(0,1000)), ylim=range(c(0,100)));
plot(time5, oct4_m6_prop_active, type="l", main="oct4 m=mcrit", xlab= "time", ylab = "Percentage of activated red posts", col="blue", xlim=range(c(0,1000)), ylim=range(c(0,100)));
plot(time6, oct4_m7_prop_active, type="l", main="oct4 m=mcrit+1", xlab= "time", ylab = "Percentage of activated red posts", col="blue", xlim=range(c(0,1000)), ylim=range(c(0,100)));

plot(time7, sox2_m6_prop_active, type="l", main="sox2 m=mcrit-1", xlab= "time", ylab = "Percentage of activated red posts", col="green", xlim=range(c(0,1000)), ylim=range(c(0,100)));
plot(time8, sox2_m7_prop_active, type="l", main="sox2 m=mcrit", xlab= "time", ylab = "Percentage of activated red posts", col="green", xlim=range(c(0,1000)), ylim=range(c(0,100)));
plot(time9, sox2_m8_prop_active, type="l", main="sox2 m=mcrit+1", xlab= "time", ylab = "Percentage of activated red posts", col="green", xlim=range(c(0,1000)), ylim=range(c(0,100)));

mtext("percentage saturation with time", outer = TRUE);

dev.off();
