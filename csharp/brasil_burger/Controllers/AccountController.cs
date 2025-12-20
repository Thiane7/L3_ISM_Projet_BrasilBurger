using Microsoft.AspNetCore.Mvc;
using Microsoft.EntityFrameworkCore;
using brasil_burger.Data;
using brasil_burger.Models;

namespace brasil_burger.Controllers
{
    public class AccountController : Controller
    {
        private readonly BrasilBurgerContext _context;

        public AccountController(BrasilBurgerContext context)
        {
            _context = context;
        }

        [HttpGet]
        public IActionResult Login() => View();

        [HttpPost]
        public async Task<IActionResult> Login(string email, string password)
        {
            var client = await _context.Clients
                .FirstOrDefaultAsync(c => c.Email == email && c.MotDePasse == password);

            if (client != null)
            {
                
                HttpContext.Session.SetInt32("ClientId", client.Id);
                HttpContext.Session.SetString("ClientNom", client.Nom);
                
                return RedirectToAction("Index", "Cart");
            }

            ViewBag.Error = "Email ou mot de passe incorrect";
            return View();
        }

        public IActionResult Logout()
        {
            HttpContext.Session.Clear();
            return RedirectToAction("Index", "Home");
        }

        [HttpPost]
        public async Task<IActionResult> Register(string nom, string email, string telephone, string password)
        {
            var nouveauClient = new Client {
                Nom = nom,
                Email = email,
                Telephone = telephone,
                MotDePasse = password
            };

            _context.Clients.Add(nouveauClient);
            await _context.SaveChangesAsync();

            return RedirectToAction("Login");
        }
        [HttpGet]
        public IActionResult Register() => View();
    }
}