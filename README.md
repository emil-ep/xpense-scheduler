# **Xpense-Scheduler**

This is an application that includes certain jobs that would run occasionally and push the processed messages to a kafka 
topic. 

## Mutual Fund Tracker Job

This is a job that would run every 5 minutes and fetches all mutual fund scheme details from an external api.
The response is processed and individual schemes are pushed to the mf_scheme topic