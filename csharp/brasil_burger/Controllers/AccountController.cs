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
            if (string.IsNullOrEmpty(email) || string.IsNullOrEmpty(password))
            {
                ViewBag.Error = "Veuillez remplir tous les champs.";
                return View();
            }

            
            var client = await _context.Clients
                .FirstOrDefaultAsync(c => c.Email.ToLower() == email.ToLower() && c.MotDePasse == password);

            if (client != null)
            {
               
                HttpContext.Session.SetInt32("ClientId", client.Id);
                HttpContext.Session.SetString("ClientNom", client.Nom);
               
                return RedirectToAction("Index", "Catalogue");
            }

            ViewBag.Error = "Email ou mot de passe incorrect.";
            return View();
        }


        [HttpGet]
        public IActionResult Register() => View();

        [HttpPost]
        public async Task<IActionResult> Register(string nom, string prenom, string telephone, string email, string password, string adresse)
        {
           
            var nouveauClient = new Client
            {
                Nom = nom,
                Prenom = prenom,
                Telephone = telephone,
                Email = email,
                MotDePasse = password, 
                Adresse = adresse
            };

            try 
            {
                
                if (string.IsNullOrEmpty(nom) || string.IsNullOrEmpty(prenom) || string.IsNullOrEmpty(email) || string.IsNullOrEmpty(password) || string.IsNullOrEmpty(telephone))
                {
                    ModelState.AddModelError("", "Tous les champs marqués d'une étoile sont obligatoires.");
                    return View();
                }

                
                _context.Clients.Add(nouveauClient);
                await _context.SaveChangesAsync();
                
                return RedirectToAction("Login");
            }
            catch (DbUpdateException ex)
            {
              
                if (ex.InnerException?.Message.Contains("duplicate key") == true)
                {
                    ModelState.AddModelError("", "Cet email ou ce numéro de téléphone est déjà utilisé.");
                }
                else
                {
                    ModelState.AddModelError("", "Erreur base de données : " + ex.InnerException?.Message);
                }
                return View();
            }
            catch (Exception ex)
            {
                ModelState.AddModelError("", "Une erreur inattendue est survenue : " + ex.Message);
                return View();
            }
        }


        public IActionResult Logout()
        {
            HttpContext.Session.Clear();
            return RedirectToAction("Index", "Home");
        }
    }
}