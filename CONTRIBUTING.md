# Contributing to Fluxxan

We would love for you to contribute to Fluxxan and help make it even better than it is
today! As a contributor, here are the guidelines we would like you to follow:

 - [Question or Problem?](#question)
 - [Issues and Bugs](#issue)
 - [Feature Requests](#feature)
 - [Submission Guidelines](#submit)


## <a name="question"></a> Got a Question or Problem?

If you have questions about how to *use* Fluxxan, please direct them to [StackOverflow][stackoverflow]. 

## <a name="issue"></a> Found an Issue?
If you find a bug in the source code or a mistake in the documentation, you can help us by
[submitting an issue](#submit-issue) to our [GitHub Repository][github]. Even better, you can [submit a Pull Request](#submit-pr) with a fix.

## <a name="feature"></a> Want a Feature?
You can *request* a new feature by [submitting an issue](#submit-issue) to our [GitHub
Repository][github]. If you would like to *implement* a new feature, please submit an issue with
a proposal for your work first, to be sure that we can use it. 
Please consider what kind of change it is:

* For a **Major Feature**, first open an issue and outline your proposal so that it can be
discussed. This will also allow us to better coordinate our efforts, prevent duplication of work, and help you to craft the change so that it is successfully accepted into the project.
* **Small Features** can be crafted and directly [submitted as a Pull Request](#submit-pr).

## <a name="submit"></a> Submission Guidelines

### <a name="submit-issue"></a> Submitting an Issue
Before you submit an issue, search the archive, maybe your question was already answered.

If your issue appears to be a bug, and hasn't been reported, open a new issue.
Help us to maximize the effort we can spend fixing issues and adding new
features, by not reporting duplicate issues.  Providing the following information will increase the chances of your issue being dealt with quickly:

* **Overview of the Issue**
* **Version** - what version of Fluxxan is affected (e.g. 0.1.0)
* **Motivation for or Use Case** - explain what are you trying to do and why the current behavior is a bug for you
* **OS version** - is this a problem with all android versions?
* **Related Issues** - has a similar issue been reported before?
* **Suggest a Fix** - if you can't fix the bug yourself, perhaps you can point to what might be causing the problem (line of code or commit)

You can file new issues by providing the above information [here](https://github.com/frostymarvelous/Fluxxan/issues/new).


### <a name="submit-pr"></a> Submitting a Pull Request (PR)
Before you submit your Pull Request (PR) consider the following guidelines:

* Search [GitHub](https://github.com/frostymarvelous/Fluxxan/pulls) for an open or closed PR that relates to your submission. You don't want to duplicate effort.
* Make your changes in a new git branch: `my-fix-branch master`
* Commit your changes using a descriptive commit message
* In GitHub, send a pull request to `master`.
* If we suggest changes then:
  * Make the required updates.
  * Rebase your branch and force push to your GitHub repository (this will update your Pull Request):

    ```shell
    git rebase master -i
    git push -f
    ```

That's it! Thank you for your contribution!

[github]: https://github.com/frostymarvelous/Fluxxan
[stackoverflow]: http://stackoverflow.com/questions/tagged/fluxxan