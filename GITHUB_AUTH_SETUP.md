# GitHub Authentication Setup

## Option 1: Using GitHub CLI (Easiest - if installed)

If you have GitHub CLI installed, run:
```bash
gh auth login
```

Follow the prompts to authenticate. Then you can create and push:
```bash
gh repo create famliy-payment-tracker --public --source=. --remote=origin --push
```

## Option 2: Using Personal Access Token (Recommended for HTTPS)

### Step 1: Create a Personal Access Token

1. Go to GitHub: https://github.com/settings/tokens
2. Click "Generate new token" → "Generate new token (classic)"
3. Give it a name: `famliy-payment-tracker`
4. Select scopes (check these):
   - ✅ `repo` (Full control of private repositories)
   - ✅ `workflow` (if you plan to use GitHub Actions)
5. Click "Generate token"
6. **COPY THE TOKEN** (you won't see it again!)

### Step 2: Create Repository on GitHub

1. Go to: https://github.com/new
2. Repository name: `famliy-payment-tracker`
3. Description: "Family Payment Tracker API - Spring Boot REST API"
4. Choose Public or Private
5. **DO NOT** initialize with README/gitignore/license
6. Click "Create repository"

### Step 3: Connect and Push

After creating the repository, run these commands:

```bash
git remote add origin https://github.com/EddieChang-senecaESG/famliy-payment-tracker.git
git branch -M main
git push -u origin main
```

When prompted for password, **paste your Personal Access Token** (not your GitHub password).

## Option 3: Using SSH Keys (Advanced)

If you prefer SSH:

1. Generate SSH key (if you don't have one):
   ```bash
   ssh-keygen -t ed25519 -C "eddie.chang@senecaesg.com"
   ```

2. Add SSH key to GitHub:
   - Copy the public key: `cat ~/.ssh/id_ed25519.pub`
   - Go to: https://github.com/settings/ssh/new
   - Paste and save

3. Test connection:
   ```bash
   ssh -T git@github.com
   ```

4. Add remote and push:
   ```bash
   git remote add origin git@github.com:EddieChang-senecaESG/famliy-payment-tracker.git
   git branch -M main
   git push -u origin main
   ```

## Quick Setup Commands (After Authentication)

Once authenticated, run these commands from your project directory:

```bash
cd "d:\side project\famliy-payment-tracker"
git remote add origin https://github.com/EddieChang-senecaESG/famliy-payment-tracker.git
git branch -M main
git push -u origin main
```

## Troubleshooting

### Authentication Failed
- Make sure you're using a Personal Access Token (not password) for HTTPS
- For SSH, make sure your key is added to GitHub

### Repository Already Exists
- If you've already created the repo, just run the remote and push commands

### Permission Denied
- Check your token has `repo` scope
- Verify your GitHub username is correct










