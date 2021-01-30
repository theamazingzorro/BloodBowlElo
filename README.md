# BloodBowlElo
 
An ELO System with simple Command Line Interface

## Current Commands

- `load 'filename'` : loads selected league [only league file currently maintained: 'ftc']
- `save`
- `saveas 'filename'`
- `ewleague 'filename'`
- `exit` : exits without saving
- `add 'teamname'`
- `addelo 'teamname' 'startingELO' 'div'`
- `delete 'teamname'`
- `setdiv 'teamname' 'newdiv'`
- `freedivs` : moves all teams to div -1
- `closediv 'div'` : sets gamesplayed to 0 and div to -1 for all teams in that div
- `endcomp` : resets gamesplayed to 0 for all teams
- `regresstomean` : move each ELO score 20% closer to 1000, done between seasons
- `byeweek 'teamname'`
- `record 'team1' 'team2' 'outcome' 'week'` : records a match and updates ELOs, outcomes must be 'win' if team1 won, 'loss' if team2 won, or 'draw' for a tie
- `predict 'team1' 'team2'` : shows possible outcomes of the matchup between the 2 teams
- `count` : shows number of teams in league database
- `showall` : shows all teams not hidden in div -1
- `show 'teamname'`
- `divranking 'div'` : show teams in selected div, sorted by elo
- `ranking` : show all teams not hidden in div -1 sorted by elo
- `gamesplayed` : show all teams not hidden in div -1 sorted by gamesplayed
- `playoffpreds` : extended prompt for predicting overall winner of a bracket [assumes normal single-elimination bracket with 2^n teams]
