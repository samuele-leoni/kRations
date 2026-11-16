# Commit instructions
- main - used for releases, when a release is accepted gets merged from develop to the main branch
- develop - used as the main branch to release the various features or fixes corresponding to the tasks. It's also the starting branch to create the feature branches
- feature/X-NameOfTheFeature - where X is the number related to that feature and NameOfTheFeature is the name of the task, it basically means that all the feature branches will be contained in the feature folder and then, when they get closed, they get merged into develop
- Whenever you finish a feature you should create a pull request that includes "Closes #X" as a description, so that the issue gets automatically closed, for example if the issue number is 5 you should write Closes #5

# Project structure
The project packages are structured as follows:
- {package_name}/{feature_name}/ui - contains the UI KMP compose code and the ViewModels for the feature
- {package_name}/{feature_name}/data - contains the data layer for the feature, including the repository and the data sources
- {package_name}/{feature_name}/domain - contains the domain layer for the feature, including the use cases and the models
- {package_name}/{feature_name}/di - contains the dependency injection module for the feature
- {package_name}/core - contains the core module for the project, including the common code and utilities used across the features

# QUOTES

# Samuele Giovanni Leoni
### Quote
"Considerate la vostra semenza:\
fatti non foste a viver come bruti,\
ma per seguir virtute e canoscenza"\
Dante, Inferno Canto XXVI vv.118-120

---

# Luca Carlo
### Quote
"Insanity is doing the same thing over and over again and expecting different results."\
Rita Mae Brown, Sudden Death (1983)


---

# Marco Torres 
### Quote
"Life is like a bicycle. To keep your balance, you must keep moving." 
  February 5, 1930 Albert Einstein
