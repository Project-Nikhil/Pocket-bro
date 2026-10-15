import { useState } from 'react';
import { Download, Heart, Menu, X, Smartphone, Sparkles } from 'lucide-react';
import { APP_CONFIG } from '../config/appConfig';

interface NavbarProps {
  onOpenBuildGuide?: () => void;
}

export const Navbar = ({ onOpenBuildGuide }: NavbarProps) => {
  const [mobileMenuOpen, setMobileMenuOpen] = useState(false);

  const closeMenu = () => setMobileMenuOpen(false);

  return (
    <header style={{
      position: 'sticky',
      top: 0,
      zIndex: 100,
      backgroundColor: 'rgba(20, 17, 15, 0.88)',
      backdropFilter: 'blur(16px)',
      borderBottom: '1px solid var(--border-medium)',
      transition: 'background-color 0.3s ease',
    }}>
      <div className="container" style={{
        display: 'flex',
        alignItems: 'center',
        justifyContent: 'space-between',
        height: '72px',
      }}>
        {/* Brand */}
        <a href="#" style={{ display: 'flex', alignItems: 'center', gap: '10px' }} onClick={closeMenu}>
          <div style={{
            width: '38px',
            height: '38px',
            borderRadius: '12px',
            background: 'linear-gradient(135deg, #2A211B 0%, #1A1412 100%)',
            border: '1px solid var(--border-medium)',
            display: 'flex',
            alignItems: 'center',
            justifyContent: 'center',
            color: 'var(--accent-amber)',
            boxShadow: '0 2px 10px rgba(0,0,0,0.3)',
          }}>
            <Heart size={20} fill="#F5C58A" color="#F5C58A" />
          </div>
          <div>
            <div style={{
              fontSize: '1.18rem',
              fontWeight: 700,
              letterSpacing: '-0.02em',
              color: 'var(--text-cream)',
              display: 'flex',
              alignItems: 'center',
              gap: '6px',
            }}>
              Pocket Brother
              <span style={{ color: '#F87171', fontSize: '0.95rem' }}>♡</span>
            </div>
            <div style={{ fontSize: '0.72rem', color: 'var(--text-muted)', marginTop: '-2px' }}>
              android companion
            </div>
          </div>
        </a>

        {/* Desktop Navigation Links */}
        <nav style={{
          display: 'none',
          alignItems: 'center',
          gap: '28px',
        }} className="desktop-nav">
          <a href="#features" className="nav-link">Features</a>
          <a href="#demo" className="nav-link">Chat Modes</a>
          <a href="#install" className="nav-link">How to Install</a>
          <a href="#privacy" className="nav-link">Privacy</a>
          <a href="#faq" className="nav-link">FAQ</a>
          {onOpenBuildGuide && (
            <button
              onClick={onOpenBuildGuide}
              style={{
                color: 'var(--accent-amber)',
                fontSize: '0.875rem',
                display: 'inline-flex',
                alignItems: 'center',
                gap: '5px',
                padding: '6px 12px',
                borderRadius: 'var(--radius-sm)',
                border: '1px dashed rgba(245, 197, 138, 0.4)',
                background: 'rgba(245, 197, 138, 0.05)',
                transition: 'all 0.2s',
              }}
            >
              <Sparkles size={14} />
              Build Guide
            </button>
          )}
        </nav>

        {/* Desktop CTA */}
        <div style={{ display: 'none', alignItems: 'center', gap: '12px' }} className="desktop-nav">
          <a
            href={APP_CONFIG.apkDownloadPath}
            download={APP_CONFIG.apkFileName}
            className="btn-primary"
            style={{ padding: '10px 20px', fontSize: '0.9rem' }}
          >
            <Download size={16} />
            Download APK
          </a>
        </div>

        {/* Mobile Hamburger Toggle */}
        <button
          onClick={() => setMobileMenuOpen(!mobileMenuOpen)}
          aria-label="Toggle navigation menu"
          style={{
            display: 'flex',
            alignItems: 'center',
            justifyContent: 'center',
            width: '42px',
            height: '42px',
            borderRadius: 'var(--radius-md)',
            background: 'var(--surface-card)',
            border: '1px solid var(--border-medium)',
            color: 'var(--text-cream)',
          }}
          className="mobile-toggle"
        >
          {mobileMenuOpen ? <X size={22} /> : <Menu size={22} />}
        </button>
      </div>

      {/* Mobile Menu Dropdown */}
      {mobileMenuOpen && (
        <div style={{
          padding: '20px 24px 28px',
          background: 'var(--bg-secondary)',
          borderBottom: '1px solid var(--border-medium)',
          display: 'flex',
          flexDirection: 'column',
          gap: '16px',
        }}>
          <a href="#features" onClick={closeMenu} className="mobile-nav-link">Features</a>
          <a href="#demo" onClick={closeMenu} className="mobile-nav-link">Chat Modes</a>
          <a href="#install" onClick={closeMenu} className="mobile-nav-link">How to Install</a>
          <a href="#privacy" onClick={closeMenu} className="mobile-nav-link">Privacy</a>
          <a href="#faq" onClick={closeMenu} className="mobile-nav-link">FAQ</a>
          {onOpenBuildGuide && (
            <button
              onClick={() => { closeMenu(); onOpenBuildGuide(); }}
              style={{
                textAlign: 'left',
                color: 'var(--accent-amber)',
                fontSize: '0.95rem',
                display: 'flex',
                alignItems: 'center',
                gap: '8px',
                padding: '10px 0',
              }}
            >
              <Sparkles size={16} />
              Brother's Build & Deploy Guide
            </button>
          )}

          <div style={{ paddingTop: '8px', borderTop: '1px solid var(--border-subtle)' }}>
            <a
              href={APP_CONFIG.apkDownloadPath}
              download={APP_CONFIG.apkFileName}
              onClick={closeMenu}
              className="btn-primary"
              style={{ width: '100%', justifyContent: 'center' }}
            >
              <Smartphone size={18} />
              Download APK for Android
            </a>
          </div>
        </div>
      )}

      <style>{`
        @media (min-width: 860px) {
          .desktop-nav {
            display: flex !important;
          }
          .mobile-toggle {
            display: none !important;
          }
        }
        .nav-link {
          color: var(--text-muted);
          font-size: 0.925rem;
          font-weight: 500;
          transition: color 0.2s ease;
        }
        .nav-link:hover {
          color: var(--accent-amber);
        }
        .mobile-nav-link {
          color: var(--text-cream);
          font-size: 1.05rem;
          font-weight: 500;
          padding: 6px 0;
        }
      `}</style>
    </header>
  );
};
