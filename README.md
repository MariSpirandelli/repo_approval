# Repository Files Approval
#### Application responsible for checking whether the authors of source code changes got all needed approvals from the engineers responsible for the files affected by the change or not.
#### The application returns a string "Approved", if it has enough approvals or "Insufficient Approvals" if there is missing any approval of any engineer.

### Application Requirements

- Java 8

### Running 
 - download the 'repo_approval.jar' file;
 - open a command promp on the root path of 'repo_approval.jar' file;
 - then execute `java -jar repo_approval.jar`.

#### It will start to execute and you will se something like:
`$________________________________________________________________________________`

### Accepted Commands, it's flags and arguments

- `validate_approvals --approvers <file_owners_names> --changed-files <files_path>`
- `exit`

#### Both file_owners_names and files_path must be comma separated when more than one is informed.
#### The application will only finish if you press a wrong command and/or the 'exit' command. Other wise it will be waiting for new approval command to be inserted.

### Commands Examples
`$ validate_approvals --approvers alovelace,ghopper --changed-files src/com/client/follow/Follow.java`

## More Info
#### All data config as who is the owner of each file as well as files dependencies are on the application file structure to simplify the application development.
