import { render, screen } from '@testing-library/react';
import SectionLookup from './SectionLookup/SectionLookup';

test('renders learn react link', () => {
  render(<SectionLookup />);
  const linkElement = screen.getByText(/learn react/i);
  expect(linkElement).toBeInTheDocument();
});
