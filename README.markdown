This is an example build for sbt 1.0.0. (It should work with sbt 0.13.x as well).

License
-------
Written in 2017 by Lightbend, Inc.
To the extent possible under law, the author(s) have dedicated all copyright and related and neighboring rights to
this example to the public domain worldwide. This example is distributed without any warranty.
See <http://creativecommons.org/publicdomain/zero/1.0/>.


Create a new repository on GitHub. You can also add a gitignore file, a readme and a licence if you want
 Open Git Bash
Change the current working directory to your local project.
Initialize the local directory as a Git repository.
git init
Add the files in your new local repository. This stages them for the first commit.
git add .
 Commit the files that you’ve staged in your local repository.
git commit -m "initial commit"
 Copy the https url of your newly created repo
In the Command prompt, add the URL for the remote repository where your local repository will be pushed.

git remote add origin remote repository URL

git remote -v
 Push the changes in your local repository to GitHub.

git push -f origin master