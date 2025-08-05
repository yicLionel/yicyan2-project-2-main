# 2-D Mario & DonkeyKong Game
This repository is a game based on BAGEL engine from The University of Melbourne.
## How to run the game?
You can clone the whole repository, go to “src/ShadowDonkeyKong.java” and run it.





## REMINDER
Given that Final Score = (100 * number of destroyed barrel) + (30 * number of jumping over a barrel) + (3 * remaining time (in seconds)) + (100 * number of destroyed monkey)

The actual scoring system is being implemented with my assumption.
The score of level1 and level2 will be stored and calculated separately.
When game finished, the total final score is calculated by adding the score of these two 
levels together. 

//eg. If the score on top-left corner of level1 is 90, and time left is 100 seconds, the total score for level1 will be
(90 + 100 * 3) = 390. And this 390 score will continue to level2 if Mario wins level1, but will not be calculated for level2. Then, if the score on top-left corner of level2 is 500, and time
left is 120 seconds, the total score for level2 will be
(500 + 120 * 3) = 860. And we add them up to get score of 1250 finally. //

Note that if Mario is killed anywhere, the final score will be 0.

Note that if Mario touches banana, Mario will die no matter if he has hammer or blaster or not.
