import { test, expect } from '@playwright/test'

test.describe('Register page tests', () => {
  test('should display the logo image', async ({ page }) => {
    await page.goto('/register')
    const logo = await page.locator('div.v-img')
    await expect(logo).toBeVisible()
  })

  test('should redirect to /login on successful registration and duplicat user error', async ({ page }) => {
    const username = "username" + Math.floor(Math.random() * 100)
    await page.goto('/register')
    await page.fill('#username', username)
    await page.fill('#emailAddress', username + '@taskagile.com')
    await page.fill('#password', 'P@ssword123')
    await page.click('button[type="submit"]')
    
    await expect(page).toHaveURL('/login')

    await page.goto('/register')
    await page.fill('#username', username)
    await page.fill('#emailAddress', username + '@taskagile.com')
    await page.fill('#password', 'P@ssword123')
    await page.click('button[type="submit"]')

    const alert = await page.locator('div.v-alert')
    await expect(alert).toBeVisible()
  })

  test('should show an error message and stay on /register with invalid username', async ({ page }) => {
    await page.goto('/register')
    await page.fill('#username', '123')
    await page.fill('#emailAddress', 'user@example.com')
    await page.fill('#password', 'password123')
    
    const message = await page.locator('div#username-messages')
    await expect(message).toBeVisible()

    await expect(page).toHaveURL('/register')
  })
})
