# GitHub Setup Instructions

## Step 1: Create Repository on GitHub

1. Go to https://github.com/new
2. Repository name: `famliy-payment-tracker` (or your preferred name)
3. Description: "Family Payment Tracker API - Spring Boot REST API for managing family debt payments"
4. Choose Public or Private
5. **DO NOT** initialize with README, .gitignore, or license (we already have these)
6. Click "Create repository"

## Step 2: Connect and Push to GitHub

After creating the repository, run these commands (replace `YOUR_USERNAME` with your GitHub username):

### Using HTTPS (recommended):
```bash
git remote add origin https://github.com/YOUR_USERNAME/famliy-payment-tracker.git
git branch -M main
git push -u origin main
```

### Using SSH (if you have SSH keys set up):
```bash
git remote add origin git@github.com:YOUR_USERNAME/famliy-payment-tracker.git
git branch -M main
git push -u origin main
```

## Step 3: Verify

Go to your GitHub repository URL to verify all files are uploaded:
```
https://github.com/YOUR_USERNAME/famliy-payment-tracker
```

## Future Commits

After the initial push, you can use these commands for future updates:

```bash
git add .
git commit -m "Your commit message"
git push
```






