pdf("percentage_saturation_with_time_run_by_run.pdf", bg="white");

for (i in 1:200) {

filename <- paste("8/",i,"/simOutputData.csv", sep="");
cat(filename,"\n");

data <- read.csv(filename, skip = 1, sep = " ");

time <- data[[1]];
nanog_reds <- data[[2]];
nanog_shared_reds <- data[[6]];

oct4_reds <- data[[8]];
oct4_shared_reds <- data[[12]];

nanog_prop_active <- 100 * (nanog_reds + nanog_shared_reds) / 631;
oct4_prop_active <- 100 * (oct4_reds + oct4_shared_reds) / 466;

plot(time, nanog_prop_active, type="l", main="System saturation vs time", xlab = "time", ylab = "Percentage of activated red posts", col="red", xlim= range(c(0,1000)), ylim = range(c(0,100)));
par(new=TRUE);
plot(time, oct4_prop_active, type="l", main="System saturation vs time", xlab = "time", ylab = "Percentage of activated red posts", col="blue", xlim= range(c(0,1000)), ylim = range(c(0,100)));

axis(1,at=seq(0,1000,100), labels=c("0","100","200","300","400","500","600","700","800","900","1000"));
axis(2,at=seq(0,100,10), labels=c("0","10","20","30","40","50","60","70","80","90","100"));

}

dev.off();

